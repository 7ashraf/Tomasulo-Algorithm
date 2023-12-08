package main;

import java.util.HashMap;

//RegisterFile.java
public class RegisterFile {
 private HashMap<Integer, Register> registers; // Array to store register values
 private ReservationStation[] reservationStations; // Array to track the reservation station using each register

 public RegisterFile(int numRegisters) {
	 registers = new HashMap<Integer, Register>();
     for (int i = 0; i < numRegisters; i++) {
    	 
    	 Register register = new Register();
    	 register.value =5;
    	 registers.put(i, register);
		
	}
     reservationStations = new ReservationStation[numRegisters];
 }

 public int readRegister(int registerIndex) {
     // Read the value from the register
     return registers.get(registerIndex).value;
 }

 public void writeRegister(int registerIndex, int value, ReservationStation reservationStation) {
     // Write the value to the register
     registers.get(registerIndex).value = value;
     // Update the reservation station that is using this register
     reservationStations[registerIndex] = reservationStation;
 }

 public ReservationStation getReservationStation(int registerIndex) {
     // Get the reservation station using this register
     return reservationStations[registerIndex];
 }
 public HashMap<Integer, Register> getRegisters() {
     return registers;
 }
 public Register getRegister(int index) {
     if (index >= 0 && index <= registers.size()) {
         return registers.get(index);
     } else {
         throw new IllegalArgumentException("Invalid register index");
     }
 }
 public void setRegister(int index, int value) {
     if (index >= 0 && index < registers.size()) {
         registers.get(index).value = value;
     } else {
         throw new IllegalArgumentException("Invalid register index");
     }
 }

public boolean isFresh(int register) {
	// TODO Auto-generated method stub
	return registers.get(register).hold == 0;
}

public void holdRegister(int r, int tag) {
	registers.get(r).hold = tag;
}

@Override
public String toString() {
	return "RegisterFile [registers=" + registers + "]";
}
}
