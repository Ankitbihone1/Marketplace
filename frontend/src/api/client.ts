import axios from 'axios';

export const apiClient = axios.create({
  baseURL: '/api', // Adjust if you have a different API base URL
});

export const setAuthUser = (userId: string | null) => {
  if (userId) {
    apiClient.defaults.headers.common['X-User-Id'] = userId;
  } else {
    delete apiClient.defaults.headers.common['X-User-Id'];
  }
};
