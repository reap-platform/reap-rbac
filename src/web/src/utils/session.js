const SESSION_KEY = 'session';

export default {
  getSession() {
    return sessionStorage.getItem(SESSION_KEY) ? JSON.parse(sessionStorage.getItem(SESSION_KEY)) : null;
  },

  setSession(session) {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
  },
  cleanSession() {
    sessionStorage.removeItem(SESSION_KEY);
  },
  set(key, value) {
    sessionStorage.setItem(key, (value instanceof Object) ? JSON.stringify(value) : value);
  },
};
