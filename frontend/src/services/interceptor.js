import axios from "axios";
import BASE_URL from "../constants/BASE_URL";

axios.interceptors.response.use(null, (error) => {
  if (
    error.config &&
    error.response.status === 401 && // change if necessary
    !error.config.__isRetry
  ) {
    return new Promise((resolve, reject) => {
      refreshToken(axios, error.config)
        .then((result) => {
          resolve(result);
        })
        .catch((err) => {
          reject(err);
        });
    });
  }
  return Promise.reject(error);
});

const refreshToken = (axios, config) => {
  return new Promise((resolve, reject) => {
    const user = JSON.parse(localStorage.getItem("user"));
    axios
      .post(`${BASE_URL}/auth/refresh/token`, {
        username: user.username,
        refreshToken: user.refreshToken
      })
      .then((res) => {
        localStorage.setItem(
          "user",
          JSON.stringify({
            authenticationToken: res.data.authenticationToken,
            expiresAt: res.data.expiresAt,
            refreshToken: res.data.refreshToken,
            username: res.data.username
          })
        );
        config.headers.Authorization = res.data.authenticationToken;
        axios
          .request(config)
          .then((result) => {
            return resolve(result);
          })
          .catch((err) => {
            console.log(err);
            return reject(err);
          });
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

export default axios;
