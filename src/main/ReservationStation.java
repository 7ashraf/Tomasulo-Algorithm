package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

//ReservationStation.java
public class ReservationStation {
	
 private int cycle; // Cycle in which the instruction entered the reservation station
// private Instruction instruction; // The instruction in the reservation station
 private int[] sourceOperands; // Index of source registers
 private int destinationOperand; // Index of the destination register
 public boolean busy; // Flag indicating whether the reservation station is busy
 private RegisterFile registerFile; // Reference to the RegisterFile
// private int size; // Add a size field
// private Queue<Instruction> instructions;  // Queue to store instructions
 public boolean waiting = false;
 public String[] qSourceOperands;
 public String operation;
 public int A;
 public Instruction instructionReference;
 public int remainingCycles;
 public int tag;
 
 
 public ReservationStation(RegisterFile registerFile, String operation, int tag) {
     // Initialization
     this.cycle = -1; // Not in use initially
//     this.instruction = null;
     this.sourceOperands = new int[2]; // Assuming at most 2 source operands
     this.destinationOperand = -1; // Not applicable initially
     this.busy = false;
     this.registerFile = registerFile;
//     this.instructions = new LinkedList<>();
     this.qSourceOperands = new String[2];
     this.operation = operation;
     this.tag = tag;

 }
// public void setSize(int size) {
//     this.size = size;
// }

// public boolean isFull() {
//     return instructions.size() >= size;
// }

// public boolean isEmpty() {
//     return instructions.isEmpty();
// }

 public void issue(Instruction instruction, int cycle) {
     // Check if the reservation station is not full before issuing an instruction
     if (true /*!isFull()*/) {
         // Issue the instruction and update the reservation station state
         busy = true;
         String operation = instruction.getOperation();
         
         destinationOperand = instruction.getDestinationRegister();
         
         sourceOperands = instruction.getSourceRegisters();
         if(checkWaiting(sourceOperands[0])) {
        	 qSourceOperands[0] = sourceOperands[0] +"";
        	 waiting = true;
         }
         if(checkWaiting(sourceOperands[1])) {
        	 qSourceOperands[1] = (sourceOperands[1]) + "";
        	 waiting = true;

         }
         this.instructionReference = instruction;
         registerFile.holdRegister(destinationOperand, this.tag);
         
         if (operation.equals("ADD") || operation.equals("SUB")) {
        	 remainingCycles =2;
         }
         remainingCycles =2;
         //instructions.offer(instruction);
//
//         // Set up source and destination operands
//         int[] sourceRegisters = instruction.getSourceRegisters();
//         for (int i = 0; i < sourceOperands.length; i++) {
//             if (i < sourceRegisters.length) {
//                 sourceOperands[i] = sourceRegisters[i];
//             } else {
//                 sourceOperands[i] = -1; // No more source operands
//             }
//         }

//         // Other initialization based on the instruction type
//         if (operation.equals("ADD") || operation.equals("SUB")) {
//             // Handle ADD/SUB instruction
//             int result = registerFile.getRegister(sourceOperands[0]) +
//                     registerFile.getRegister(sourceOperands[1]);
//             // Store the result in the reservation station
//             // ...
//         } else if (operation.equals("MUL") || operation.equals("DIV")) {
//             // Handle MUL/DIV instruction
//             int result = registerFile.getRegister(sourceOperands[0]) *
//                     registerFile.getRegister(sourceOperands[1]);
//             // Store the result in the reservation station
//             // ...
//         } else if (operation.equals("LD")) {
//             // Handle LD instruction
//             // ...
//         } else if (operation.equals("ST")) {
//             // Handle ST instruction
//             // ...
//         } else if (operation.equals("BNEZ")) {
//             // Handle BNEZ instruction
//             // ...
//         }

         System.out.println("Instruction issued: " + instruction.getOperation() +
                 ", Dest: R" + destinationOperand +
                 ", Sources: R" + sourceOperands[0] + ", R" + sourceOperands[1] + ", waiting" + waiting+
                 ", Cycle: " + cycle);
     } else {
         System.out.println("Reservation station is full. Cannot issue instruction.");
     }
 }
 
 public boolean checkWaiting(int register) {
	 return !registerFile.isFresh(register);
 }
 
 

     
 

 public boolean isReady(RegisterFile registerFile) {
     // Check if all source operands are ready
     for (int sourceOperand : sourceOperands) {
         if (sourceOperand != -1 && registerFile.getReservationStation(sourceOperand).isBusy()) {
             return false; // Source operand is not ready
         }
     }
     return true; // All source operands are ready
 }

 public void execute() {
     // Execute the instruction
     // Update the destination operand value based on the operation type
     // ...

     // Mark the reservation station as not busy after execution
     busy = false;
 }

// public void writeResultToRegisterFile(RegisterFile registerFile) {
//	 int result = instructions.poll().getResult(); // Adjust this based on your instruction
//
//     // Write the result to the register file
//     registerFile.writeRegister(destinationOperand, result/* result */, this);
// }

 // Other methods as needed
 // ...

 // Getters and setters
 public int getCycle() {
     return cycle;
 }

 public boolean isBusy() {
     return busy;
 }
 public boolean isReady() {
     // Check if all source operands are ready
     for (int sourceOperand : sourceOperands) {
         if (sourceOperand != -1 && registerFile.getReservationStation(sourceOperand).isBusy()) {
             return false; // Source operand is not ready
         }
     }
     return true; // All source operands are ready
 }
// 
// public String getOperation() {
//     return instruction.getOperation();
// }
 
 public int getDestinationOperand() {
     return destinationOperand;
 
 
 }
// 
// public Instruction getInstruction() {
//     return instruction;
// }
 public int[] getSourceOperands() {
     return sourceOperands;
 }

@Override
public String toString() {
	return "ReservationStation [sourceOperands=" + Arrays.toString(sourceOperands) + ", destinationOperand="
			+ destinationOperand + ", busy=" + busy + ", waiting=" + waiting + ", qSourceOperands="
			+ Arrays.toString(qSourceOperands) + ", operation=" + operation + ", instructionReference="
			+ instructionReference + ", remainingCycles=" + remainingCycles + "]";
}
 
 
 
 
 
 
 
 
 
}

