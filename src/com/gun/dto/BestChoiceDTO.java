package com.gun.dto;

public class BestChoiceDTO {

	// 长枪DTO
	public EquipmentFormDTO firstGunDTO;
	// 长枪数量
	public int firstGunQulty;
	// 余下的合适的枪型
	public EquipmentFormDTO reminderGun;
	// 余下的需要添加射孔弹的长度
	public double reminder;
	// 余下的可以补给上一段夹层枪的长度
	public double leftLength;
	// 余下的可以补给上一段夹层枪的最大长度，这里如果leftLength与maxLeftLength相同，代表已选了最长枪，没差别
	public double maxLeftLength;

	public BestChoiceDTO(EquipmentFormDTO firstGunDTO, int firstGunQulty,
			EquipmentFormDTO reminderGun, double reminder,double leftLength,
			double maxLeftLength) {
		super();
		this.firstGunDTO = firstGunDTO;
		this.firstGunQulty = firstGunQulty;
		this.reminderGun = reminderGun;
		this.reminder = reminder;
		this.leftLength = leftLength;
		this.maxLeftLength = maxLeftLength;
	}

	public EquipmentFormDTO getFirstGunDTO() {
		return firstGunDTO;
	}

	public void setFirstGunDTO(EquipmentFormDTO firstGunDTO) {
		this.firstGunDTO = firstGunDTO;
	}

	public int getFirstGunQulty() {
		return firstGunQulty;
	}

	public void setFirstGunQulty(int firstGunQulty) {
		this.firstGunQulty = firstGunQulty;
	}

	public EquipmentFormDTO getReminderGun() {
		return reminderGun;
	}

	public void setReminderGun(EquipmentFormDTO reminderGun) {
		this.reminderGun = reminderGun;
	}

	public double getReminder() {
		return reminder;
	}

	public void setReminder(double reminder) {
		this.reminder = reminder;
	}

	public double getLeftLength() {
		return leftLength;
	}

	public void setLeftLength(double leftLength) {
		this.leftLength = leftLength;
	}

	public double getMaxLeftLength() {
		return maxLeftLength;
	}

	public void setMaxLeftLength(double maxLeftLength) {
		this.maxLeftLength = maxLeftLength;
	}
	
}
