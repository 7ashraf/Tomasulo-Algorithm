package main;

//TomasuloSimulator.java
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class TomasuloSimulator {
 private RegisterFile registerFile;
 private List<ReservationStation> reservationStations;
 public DisplayStation display;
 
 private List<Instruction> instructions;
 private int cycle;

 private int addLatency;
 private int mulLatency;
 private int divLatency;
 private int loadLatency;
 private int storeLatency;
 private int branchLatency;
 public int addSubStationsCount = 3;
 public int mulDivStationsCount = 2;
 public int lSStaionsCount =3;
 public static boolean isBranchWait=false;
 public int PC=0;
private Memory memory;

 public TomasuloSimulator(int numRegisters, List<ReservationStation> reservationStations, List<Instruction> instructions) {
     this.registerFile = new RegisterFile(numRegisters);
     this.reservationStations = reservationStations;
     this.instructions = instructions;
     this.cycle = 0;

 }
 
 public TomasuloSimulator() {
     this.instructions = new ArrayList<>();
     this.display = DisplayStation.getInstance();
     memory = Memory.getInstance();
 }

 public void runSimulation() {
     while (!allInstructionsCompleted()) {
         // Issue instructions to reservation stations
         //issueInstructions();
    	 if(!isBranchWait) {
    	 issueSingleInstruction();
    	 }

         // Execute instructions in reservation stations
         //executeInstructions();
    	 executeSingleInstruction();

         // Increment cycle
         cycle++;

         // Print the state of the simulation
         printSimulationState();
     }
 }
 
 private void issueSingleInstruction() {
     for (Instruction instruction : instructions) {
         // Find a free reservation station
    	 String operation = instruction.getOperation();
         ReservationStation freeStation = findFreeReservationStation(operation);
         if(operation.equals("BNEZ")) {
        	 isBranchWait=true;
         }
         //find not issued instruction and issue it 
         if (instruction.getState() == -1) {
        	 //issue ins
             // Issue the instruction to the reservation station
             if (freeStation != null) {
                
                 freeStation.issue(instruction, cycle);
                 instruction.setState(0);
                 freeStation.instructionReference.setState(0);
                 return;
             }
         }

     }
 }
 
 private void executeSingleInstruction() {
	 
	 
	 //the execute instruction should execute instructions from the reservatoin system not from the instructions queue 
	 //the instructions are executed based on a certain state
	 //loop through all reservation stations and check if any instructions has this state and begin to execute them, maybe add instruction in execution queue or executing state 
	 //states to begin execution: Instruction source operands are and fresh -waiting==false-, or instruction state == 1 ie executing, 
	 //TODO should be in the execution unit 
	
	 for (ReservationStation station : reservationStations) {
		 //implement latencies later
		 //i think waiting is enough, either waiting or executing, wrong 
		 if(station.instructionReference == null) continue;
		 if(station.waiting == false && station.instructionReference.getState() < 2) {
			 //TODO implement latency
			 if(station.remainingCycles > 0) {
				 station.remainingCycles --;
			 }else {
				 station.calculateResult();
                 //TODO should write to  bus and bus notifies then all waiting elemtns on tag should take
                 station.instructionReference.setResult(station.result);
				 station.instructionReference.setState(2);
			 }
			
			 
			 // write back on this cycle and remove instruction and publish results to bus 
			 // instruction is completed
		 }else if(station.waiting == false && station.instructionReference.getState() == 2) {
            System.out.println("Writing back Instruction " + station.instructionReference);
            display.printWritingBackInstruction(station.instructionReference);
			 //store in destination register
			 int destinationRegister = station.instructionReference.getDestinationRegister();
			 int result = station.instructionReference.getResult();
            if(!station.type.equals("lS"))
			 registerFile.setRegister(destinationRegister, result);
			 registerFile.getRegister(destinationRegister).hold =0;
			 // remove instruction 
			 removeInstruction(station);
			 
			 //TODO check for instructions waiting on results
			 // loop through reservation stations, if waiting is true, and waiting register is result set waiting false and update source registers
			 
			 for (ReservationStation wbStation : reservationStations) {
				 if(wbStation.waiting == true) {
					 
					 if(wbStation.qSourceOperands[0]!=null) {
					 if(wbStation.qSourceOperands[0].equals(destinationRegister+"")) {
                        wbStation.getSourceOperands()[0] = result;
                        //registerFile.writeRegister(wbStation.getSourceOperands()[0], result, station);
						 wbStation.waiting = false;
						 
					 }
					 }
					 if(wbStation.qSourceOperands[1]!=null) {
					 if(wbStation.qSourceOperands[1].equals(destinationRegister+"")) {
                        wbStation.getSourceOperands()[1] = result;

                         //registerFile.writeRegister(wbStation.getSourceOperands()[1], result, station);

						 wbStation.waiting = false;

						 
					 }
					 }
				 }
			 }
			 
			 
			 
		 }
	 
	 }

	 
	 
	 
//	 for (Instruction instruction : instructions) {
//         // Find a free reservation station
//         ReservationStation freeStation = findFreeReservationStation();
//         //find not issued instruction and issue it 
//         if (instruction.getState() == null) {
//        	 //issue ins
//             // Issue the instruction to the reservation station
//             if (freeStation != null) {
//                 freeStation.issue(instruction, cycle);
//                 instruction.setState("issued");
//             }
//         }
//
//     }
 }

private void removeInstruction(ReservationStation station) {
	// TODO Auto-generated method stub
	instructions.remove(station.instructionReference);
    station.busy = false;
    //TODO reservationStations.remove(station);
    
	
}

// private void issueInstructions() {
//     // Iterate through instructions and issue them to reservation stations
//     for (Instruction instruction : instructions) {
//         // Find a free reservation station
//         ReservationStation freeStation = findFreeReservationStation(instruction.getOperation());
//         
//         // Issue the instruction to the reservation station
//         if (freeStation != null) {
//             freeStation.issue(instruction, cycle);
//         }
//     }
// }

// private void executeInstructions() {
//	    // Execute instructions in reservation stations
//	    for (ReservationStation reservationStation : reservationStations) {
//	        reservationStation.execute();
//	        // Mark the corresponding instruction as completed
//	        Instruction instruction = reservationStation.getInstruction();
//	        if (instruction != null) {
//	            instruction.setCompleted(true);
//	        }
//	    }
//	}
// private void executeInstructions() {
//     // Execute instructions in reservation stations
//     for (ReservationStation reservationStation : reservationStations) {
//         reservationStation.execute();
//
//         // Handle completion of instruction
//         Instruction completedInstruction = reservationStation.getCompletedInstruction();
//         if (completedInstruction != null) {
//             // Update the register file with the result
//             int result = completedInstruction.getResult();
//             int destinationOperand = completedInstruction.getDestinationOperand();
//             registerFile.setRegister(destinationOperand, result);
//         }
//     }
// }
 private boolean allInstructionsCompleted() {
     // Check if all instructions have been completed
     for (Instruction instruction : instructions) {
         if (!instruction.isCompleted()) {
             return false;
         }
     }
     return true;
 }

 private ReservationStation findFreeReservationStation(String operation) {
     // Find and return a free reservation station
     for (ReservationStation reservationStation : reservationStations) {
    	 if(operation.equals("ADD") || operation.equals("SUB") || operation.equals("BNEZ")) {
    		 if (!reservationStation.isBusy() && reservationStation.type.equals("addSub")) {
                 return reservationStation;
             }
    	 }else if(operation.equals("MUL") || operation.equals("DIV")) {
    		 if (!reservationStation.isBusy() && reservationStation.type.equals("mulDiv")) {
                 return reservationStation;
             }
    	 }else if(operation.equals("LD") || operation.equals("SD")) {
    		 if (!reservationStation.isBusy() && reservationStation.type.equals("lS")) {
                 return reservationStation;
             }
    		 
    	 }
    	 //TODO handle branches and other ins types
         
     }
     return null; // No free reservation station available
 }

 private void printSimulationState() {
	    System.out.println("Cycle: " + cycle);
        display.printRegisterFile(registerFile);
        display.printReservationStations(reservationStations);
        display.printMemory(memory);
	    System.out.println("Reservation Stations:");
	    for (ReservationStation reservationStation : reservationStations) {
	        System.out.println("Station " + reservationStations.indexOf(reservationStation) + ": " +
	                "Busy: " + reservationStation.isBusy() +
	                ", Operation: " + reservationStation.operation +
	                ", Dest: R" + reservationStation.getDestinationOperand() +
	                ", Sources Values: " + reservationStation.getSourceOperands()[0] +
	                ", " + reservationStation.getSourceOperands()[1] +
            ", waiting" + reservationStation.waiting);
;
	    }
	    System.out.println("Register File:");
	    for (int i = 0; i < registerFile.getRegisters().size(); i++) {
	        System.out.println("R" + i + ": " +"hold: " + registerFile.getRegister(i).hold+ " value: " +registerFile.readRegister(i));
	    }
	    System.out.println("=======================================");
	}
 
 public void getUserInput() {
     Scanner scanner = new Scanner(System.in);

     // Set up the simulator parameters
     System.out.print("Enter the number of registers: ");
     //int numRegisters = scanner.nextInt();
     int numRegisters = 15;


     System.out.print("Enter the number of reservation stations: ");
     //int numReservationStations = scanner.nextInt();
     int numReservationStations = 10;

     // Initialize the simulator with user-defined parameters
     initializeSimulator(numRegisters, numReservationStations);

     // Set instruction latencies
     setInstructionLatencies(scanner);

     // Set station and buffer sizes
     //setStationBufferSizes(scanner);

     // Pre-load cache and register file
     preLoadCacheAndRegisterFile(scanner);

     // Load instructions from a file
     System.out.print("Enter the path to the input file: ");
     //String filePath = scanner.next();
     String filePath = "";

     loadInstructionsFromFile(filePath);

     // Close the scanner
     //scanner.close();
 }
 private void setInstructionLatencies(Scanner scanner) {
     System.out.print("Enter latency for ADD instruction: ");
     //addLatency = scanner.nextInt();
     addLatency = 1;

     System.out.print("Enter latency for MUL instruction: ");
     //mulLatency = scanner.nextInt();
     mulLatency = 1;

     System.out.print("Enter latency for DIV instruction: ");
     //divLatency = scanner.nextInt();
     divLatency = 1;

     System.out.print("Enter latency for LD instruction: ");
     //loadLatency = scanner.nextInt();
     loadLatency = 1;

     System.out.print("Enter latency for ST instruction: ");
     //storeLatency = scanner.nextInt();
     storeLatency = 1;

     System.out.print("Enter latency for BNEZ instruction: ");
     //branchLatency = scanner.nextInt();
     branchLatency = 1;

 }
 private void setStationBufferSizes(Scanner scanner) {
     System.out.print("Enter the size of all reservation stations: ");
     //int stationSize = scanner.nextInt();
     int stationSize = 2;

     // Adjust this based on your ReservationStation implementation
     for (ReservationStation reservationStation : reservationStations) {
         //reservationStation.setSize(stationSize);
     }
 }
 private void preLoadCacheAndRegisterFile(Scanner scanner) {
     // Add logic to pre-load cache and register file if needed
     // ...
 }
 
 private void initializeSimulator(int numRegisters, int numReservationStations) {
     // Set up the simulator with the specified number of registers and reservation stations
     registerFile = new RegisterFile(numRegisters);
     reservationStations = new ArrayList<>();
     for (int i = 0; i < addSubStationsCount; i++) {
    	 System.out.println(i);
         reservationStations.add(new ReservationStation(registerFile, "mulDiv", i+1));
     }
     for (int i = 0; i < mulDivStationsCount; i++) {
         reservationStations.add(new ReservationStation(registerFile, "addSub", i+1));
     }
     for (int i = 0; i < lSStaionsCount; i++) {
         reservationStations.add(new ReservationStation(registerFile, "lS", i+1));
     }
 }
 private void loadInstructions(Scanner scanner) {
     // Load instructions from the user
     System.out.println("Enter MIPS instructions (one per line, type 'done' to finish):");
     String input;
     while (!(input = scanner.nextLine()).equalsIgnoreCase("done")) {
         Instruction instruction = parseInstruction(input);
         instructions.add(instruction);
     }
 }
 
//  private Instruction parseInstruction(String input) {
//      // Parse the input string and create an Instruction object
//      // Adjust this based on your specific instruction format
//      String[] parts = input.split("\\s+");
//      System.out.println(input);
//      System.out.println(parts[0] + parts[1]);
//      String operation = parts[0];
//      int destRegister = Integer.parseInt(parts[1].substring(1)); // Assuming register format like R1, R2, etc.
//      int[] sourceRegisters = new int[2];
//      //sourceRegisters[0] = Integer.parseInt(parts[0 + 2].substring(1));
//      for (int i = 0; i < sourceRegisters.length; i++) {
//          sourceRegisters[i] = Integer.parseInt(parts[i + 2].substring(1));
//      }
//      //int immediateValue = Integer.parseInt(parts[parts.length - 1]);

//      return new Instruction(operation, destRegister, sourceRegisters, 0, 2, this.registerFile);
//  }

 private Instruction parseInstruction(String input) {
    // Parse the input string and create an Instruction object
    // Adjust this based on your specific instruction format

    String[] parts = input.split("\\s+");
    System.out.println(input);

    String operation = parts[0];
    int destRegister = Integer.parseInt(parts[1].substring(1)); // Assuming register format like R1, R2, etc.
    int[] sourceRegisters = new int[2];

    if(operation.equals("BNEZ")) {
    	sourceRegisters[0]=destRegister;
    	String label=parts[2];
    	return new Instruction(operation, 0, sourceRegisters, 0, 1,label, this.registerFile);
    }
    // Check if it's an LD or SD instruction
    else
    if (operation.equals("LD") || operation.equals("SD")) {
        // For LD and SD instructions, the destination address is in the format "100"
        sourceRegisters[0] = Integer.parseInt(parts[2]); 
        int destinationAddress = Integer.parseInt(parts[2]);
        return new Instruction(operation, destRegister, sourceRegisters, destinationAddress, 2,"", this.registerFile);
    } else {
        // For other instructions
        for (int i = 0; i < sourceRegisters.length; i++) {
            sourceRegisters[i] = Integer.parseInt(parts[i + 2].substring(1));
        }
        return new Instruction(operation, destRegister, sourceRegisters, 0, 2, "",this.registerFile);
    }
}


 private void loadInstructionsFromFile(String filePath) {
     try {
    	 URL url = getClass().getResource("ins");
    	 File file = new File(url.getPath());
         Scanner fileScanner = new Scanner(file);

         while (fileScanner.hasNextLine()) {
             String line = fileScanner.nextLine();

             Instruction instruction = parseInstruction(line);
             instructions.add(instruction);
         }

         fileScanner.close();
     } catch (FileNotFoundException e) {
         System.err.println("File not found: " + filePath);
         e.printStackTrace();
     }
 }

 public static void main(String[] args) {
     TomasuloSimulator simulator = new TomasuloSimulator();
     simulator.getUserInput();
     simulator.runSimulation();
 }
}

//TODO 
//TODO update reservation station after instruction completes
//TODO update loop to termimnate after reservation stations finish executing
//TODO check for register file values and use correct values in arethmetic operations
//TODO check for waiting q dest and source when issueing ins
//TODO remove reservation station after instruction complete