package de.codesourcery.jasm16.emulator;

public interface IDevice {

	/**
	 * Invoked after this device has been added to an emulator.
	 * @param emulator
	 */
	public void afterAddDevice(IEmulator emulator);
	
	/**
	 * Invoked before this device is removed from
	 * an emulator.
	 * 
	 * @param emulator
	 */
	public void beforeRemoveDevice(IEmulator emulator);
	
	/**
	 * Returns this devices hardware ID.
	 * 
	 * @return 32-bit value (only the lower 32 bits are used) 
	 */
	public long getHardwareID();
	
	/**
	 * Returns this devices hardware version.
	 * 
	 * @return 16-bit value (only the lower 16 bits are used)
	 */
	public int getHardwareVersion();
	
	/**
	 * Returns this devices manufacturer ID.
	 * 
	 * @return 32-bit value (only the lower 32 bits are used) 
	 */
	public long getManufacturer();
	
	/**
	 * Handle a software interrupt triggered by the application.
	 * @param emulator
	 */
	public void handleInterrupt(IEmulator emulator);
}