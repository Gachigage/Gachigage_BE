package com.gachigage.global;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 API 응답 객체")
public record ApiResponse<T>(
	@Schema(description = "API 응답 코드", example = "200") int code,
	@Schema(description = "API 응답 상태", examples = {"success", "fail"}) ApiStatus status,
	@Schema(description = "API 응답 메시지", example = SUCCESS_MESSAGE) String message,
	// TODO: implementation으로 예상 data 값 형식 추후 작성
	@Schema(description = "API 응답 데이터") T data) {
	private static final String SUCCESS_MESSAGE = "성공적으로 처리되었습니다.";

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(200, ApiStatus.success, SUCCESS_MESSAGE, null);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(200, ApiStatus.success, SUCCESS_MESSAGE, data);
	}

	public static <T> ApiResponse<T> fail(int errorCode, String message) {
		return new ApiResponse<>(errorCode, ApiStatus.fail, message, null);
	}
}
