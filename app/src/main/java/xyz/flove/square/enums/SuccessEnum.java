package xyz.flove.square.enums;

public enum SuccessEnum {
	// 方：西北方，西南方，东北方，东南方
	// 拉：南纬， 中纬，北纬，西经，中径，东径
	// 西北东南斜：（左三斜，左四斜，中斜，右四斜，右三斜）
	// 东北西南斜：（左三斜，左四斜，中斜，右四斜，右三斜）
	WEST_NORTH_SQUARE, WEST_SORTH_SQUARE, EAST_NORTH_SQUARE, EAST_SORTH_SQUARE,
	SORTH_LAT_LINE, CENTER_LAT_LINE, NORTH_LAT_LINE,
	WEST_LON_LINE, CENTER_LON_LINE, EAST_LON_LINE,
	WNES_LEFT_THREE_OBLIQUE, WNES_LEFT_FOUR_OBLIQUE, WNES_CENTER_OBLIQUE, WNES_RIGHT_FOUR_OBLIQUE, WNES_RIGHT_THREE_OBLIQUE,
	ENWS_LEFT_THREE_OBLIQUE, ENWS_LEFT_FOUR_OBLIQUE, ENWS_CENTER_OBLIQUE, ENWS_RIGHT_FOUR_OBLIQUE, ENWS_RIGHT_THREE_OBLIQUE;

	public static SuccessEnum getSuccessEnum(int i) {

		for (SuccessEnum success : SuccessEnum.values()) {
			if (success.ordinal() == i) {
				return success;
			}
		}
		return null;
	}
}
