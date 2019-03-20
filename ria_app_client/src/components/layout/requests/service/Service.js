import axios from "axios";
import setJwtToken from "../../security/SetJwtToken"
import jwt_decode from "jwt-decode";
import { SET_CURRENT_USER } from "../../../../actions/types";
import { logout } from "../../../../actions/securityActions";
import store from "../../../../store"

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setJwtToken(jwtToken);
  const decode_jwtToken = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decode_jwtToken
  });

  const currentTime = Date.now() / 1000;
  if (decode_jwtToken.exp < currentTime) {
    store.dispatch(logout());
    window.location.href = "/";
  }
}
class Service {
  getRestClient() {
    if (!this.serviceInstance) {
      this.serviceInstance = axios.create({
        baseURL: '/api/files/download/',
        timeout: 10000,
        headers: {
            'Content-Type': 'application/json',
            // 'Authorization': setJwtToken(jwtToken)
          },
      });
    }
    return this.serviceInstance;
  }
}

export default (new Service());