package model;

public enum ChefStat {
	SPEED,
	SKILL;

	public static ChefStat fromString(String raw) {
		if (raw == null) return null;
		String trimmed = raw.trim().toLowerCase();
		if (trimmed.equals("speed")) return SPEED;
		if (trimmed.equals("skill")) return SKILL;
		return null;
	}
}
