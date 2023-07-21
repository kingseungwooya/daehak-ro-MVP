package daehakro.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    // AUTH_BAD_REQUEST(403, "bad request"),
    // AUTH_INVALID_TOKEN(401, "invalid token"),
    // AUTH_NOT_JOINED(405, "not joined user"),
    // AUTH_REFRESH_DOES_NOT_EXIST(401, "REFRESH_DOES_NOT_EXIST"),
    // AUTH_REFRESH_EXPIRED(401, "AUTH_REFRESH_EXPIRED"),

    UNIV_NOT_EXIST(400, "요청하신 ID의 대학정보가 없습니다."),

    EVENT_NOT_EXIST(400, "해당 EVENT를 찾을 수 없습니다."),
    EVENT_APPLICANT_ALREADY_EXIST(400, "이미 이벤트에 참여중입니다." ),
    EVENT_NOT_ENDED(403, "해당 EVENT의 마감기한이 끝나지 않았습니다."),
    EVENT_MATCH_FAIL(403, "신청자 수가 적어 matching 과정 중 오류 발생"),
    EVENT_MATCH_FULL(403, "신청인원이 마감되었습니다."),
    EVENT_APPLICANTS_NUM_FAIL(403, "해당 EVENT의 참여 인원과 신청 인원이 불일치합니다."),
    EVENT_APPLICANT_FORM_FAIL(403, "동성끼리만 신청이 가능합니다."),

    USER_USERNAME_CK_SUCCESS(200, "사용가능한 아이디입니다."),
    USER_USERNAME_CK_FAIL(500, "사융할 수 없는 아이디입니다."),

    USER_JOIN_SUCCESS(200, "회원가입에 성공하였습니다."),
    USER_JOIN_FAIL(500, "다시 시도해주세요."),

    USER_MY_INFO_SUCCESS(200,"조회 성공"),
    USER_NOT_FOUND(401,"회원이 존재하지 않습니다"),
    COIN_DOES_NOT_EXIST(500,"이벤트에 신청하려면 코인이 1개 이상 있어야 합니다."),


    CHAT_ROOM_MAKE_SUCCESS(200, "생성 성공"),
    CHAT_ROOM_MAKE_EXIST(-1,"이미 존재하는 채팅방 입니다"),
    CHAT_ROOM_CAN_NOT_TO_ME(-1,"자기 자신에게는 보낼 수 없습니다"),

    CHAT_ROOM_LIST_SUCCESS(200, "조회 성공"),

    CHAT_ROOM_DETAIL_SUCCESS(200, "조회 성공"),
    CHAT_ROOM_NOT_EXIST(-1,"존재하지 않는 채팅방입니다"),
    CHAT_ROOM_NOT_PERMISSION(-1,"접근할 수 없는 채팅방입니다"),

    KAFKA_CONSUME_ERR(500, "메시지를 불러오는과정에서 실패하였습니다."),
    KAFKA_PARTITION_KEY_ERR(-1, "Partition에 KEY(RoomId)를 넣어줘야합니다."),
    KAFKA_PARTITION_KEY_TYPE_ERR(-1, "Partition 에 KEY(RoomId)는 Integer 형식이어야 합니다.");


    private final int code;
    private final String message;

}