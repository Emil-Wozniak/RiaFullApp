import * as types from "../actions/types";

const initial_state = {
    examination_points: [],
    examination_point: {},
    errors: {}
};

export default function (state = initial_state, action) {
    switch (action.type) {
        case types.GET_EXAMINATION_POINTS:
            return {
                ...state,
                examination_points: action.payload
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
