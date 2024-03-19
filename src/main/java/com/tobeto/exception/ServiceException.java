package com.tobeto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -309140238380688123L;

	public static enum ERROR_CODES {
		FRUIT_NOT_FOUND(1, "Fruit not found."), BOX_NOT_FOUND(2, "Box not found."),
		NOT_ENOUGH_BOX(3, "Not enough box."), BOX_HAS_FRUITS(4, "Box has fruits."),
		SET_BOX_COUNT(5, "You cannot set capacity less than the number of fruits in the box.");

		private int code;
		private String message;

		private ERROR_CODES(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

	}

	private int code;
	private String message;

	public ServiceException(ERROR_CODES errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}
}
