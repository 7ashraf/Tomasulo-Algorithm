package main;

import java.util.Arrays;

//Instruction.java
public class Instruction {
 private String operation; // Operation (e.g., ADD, SUB, LD, etc.)
 private int destinationRegister; // Destination register index
 private int[] sourceRegisters; // Source register indices
 private int immediateValue; // Immediate value (if applicable)
 private int result; // Result of the instruction
 private boolean completed;
 private int state;
 public int latency;
 public RegisterFile registers;

 public Instruction(String operation, int destinationRegister, int[] sourceRegisters, int immediateValue, int latency, RegisterFile registers) {
     this.operation = operation;
     this.destinationRegister = destinationRegister;
     this.sourceRegisters = sourceRegisters;
     this.immediateValue = immediateValue;
     this.state = -1;
     this.latency = latency;
     this.registers = registers;
 }
 public int getResult() {
     // Return the result of the instruction
     return result;
 }
 @Override
public String toString() {
	return "Instruction [operation=" + operation + ", destinationRegister=" + destinationRegister + ", sourceRegisters="
			+ Arrays.toString(sourceRegisters) + ", result=" + result + ", completed=" + completed + ", state=" + state
			+ ", latency=" + latency + "]";
}
public void setState(int state) {
	 this.state = state;
 }
 public boolean isCompleted() {
     return completed;
 }
 public void setCompleted(boolean completed) {
     this.completed = completed;
 }
 // Getters for the instruction fields
 public String getOperation() {
     return operation;
 }

 public int getDestinationRegister() {
     return destinationRegister;
 }

 public int[] getSourceRegisters() {
     return sourceRegisters;
 }

 public int getImmediateValue() {
     return immediateValue;
 }
 
 
 //TODO should be inside execution station
 
 public void calculateResult() {
     // Perform the operation based on the instruction type
     // Adjust this based on your specific instruction set
     switch (operation) {
         case "ADD":
             result = registers.getRegister(sourceRegisters[0]).value + registers.getRegister(sourceRegisters[1]).value ;
             break;
         case "SUB":
             result = registers.getRegister(sourceRegisters[0]).value - registers.getRegister(sourceRegisters[1]).value ;
             break;
         case "MUL":
             result = registers.getRegister(sourceRegisters[0]).value * registers.getRegister(sourceRegisters[1]).value ;
             break;
         case "DIV":
             // Check for division by zero before performing the operation
             if (sourceRegisters[1] != 0) {
                 result = registers.getRegister(sourceRegisters[0]).value / registers.getRegister(sourceRegisters[1]).value ;
             } else {
                 // Handle division by zero (throw an exception, set result to a special value, etc.)
                 throw new ArithmeticException("Division by zero");
             }
             break;
         case "FADD":
             // Assuming floating-point addition
             // Adjust this based on your specific floating-point format
             float floatResult = Float.intBitsToFloat(sourceRegisters[0]) +
                                 Float.intBitsToFloat(sourceRegisters[1]);
             result = Float.floatToIntBits(floatResult);
             break;
         // Add cases for other operations (e.g., FDIV, FMUL, etc.)
         default:
             // Handle unknown operation or throw an exception
             throw new UnsupportedOperationException("Unsupported operation: " + operation);
     }
 }
public int getState() {
	// TODO Auto-generated method stub
	return this.state;
}
}
