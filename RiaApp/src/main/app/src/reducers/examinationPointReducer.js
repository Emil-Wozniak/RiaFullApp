import * as types from "../actions/types";

const initial_state = {
    examination_point: {},
    examination_points: [],
    errors: {}
};

export default function (state = initial_state, action) {
    switch (action.type) {
        case types.GET_EXAMINATION_POINTS:
            return {
                ...state,
                examination_point: action.payload
            };
        case types.GET_EXAMINATION_POINT:
            return {
                ...state,
                examination_point: action.payload
            };
        case types.GET_ERRORS:
            return {
                ...state,
                errors: action.message
            };
        default:
            return state;

    }
}
