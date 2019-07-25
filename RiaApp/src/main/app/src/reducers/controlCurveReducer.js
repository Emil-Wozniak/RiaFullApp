import * as types from "../actions/types";

const initial_state = {
    control_curves: [],
    control_curve: {},
    errors: {}
};

export default function (state = initial_state, action) {
    switch (action.type) {
        case types.GET_CONTROL_CURVES:
            return {
                ...state,
                control_curves: action.payload
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
