import axios, {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";

interface CustomAxiosRequestConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
}

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});

let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

const subscribeTokenRefresh = (cb: (token: string) => void) => {
  refreshSubscribers.push(cb);
};

const onRefreshed = (token: string) => {
  refreshSubscribers.forEach((cb) => cb(token));
  refreshSubscribers = [];
};

api.interceptors.request.use((config: CustomAxiosRequestConfig) => {
  const token = localStorage.getItem("accessToken");
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config as CustomAxiosRequestConfig;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (!isRefreshing) {
        isRefreshing = true;
        try {
          const res: AxiosResponse<{ accessToken: string }> = await axios.get(
            `${import.meta.env.VITE_BASE_URL}/auth/refresh`,
            { withCredentials: true }
          );

          const newToken = res.data.accessToken;
          localStorage.setItem("accessToken", newToken);

          isRefreshing = false;
          onRefreshed(newToken);

          originalRequest.headers = originalRequest.headers || {};
          originalRequest.headers.Authorization = `Bearer ${newToken}`;

          return api(originalRequest);
        } catch (err) {
          isRefreshing = false;
          localStorage.removeItem("accessToken");
          window.location.href = "/login";
          return Promise.reject(err);
        }
      }

      // Wait for the new token
      return new Promise((resolve) => {
        subscribeTokenRefresh((token: string) => {
          originalRequest.headers = originalRequest.headers || {};
          originalRequest.headers.Authorization = `Bearer ${token}`;
          resolve(api(originalRequest));
        });
      });
    }

    return Promise.reject(error);
  }
);

export const fetchData = async <T>(endpoint: string): Promise<T> => {
  const res: AxiosResponse<T> = await api.get(endpoint);
  return res.data;
};

export const createData = async <T, U>(
  endpoint: string,
  data: U
): Promise<T> => {
  const res: AxiosResponse<T> = await api.post(endpoint, data);
  return res.data;
};

export const updateData = async <T, U>(
  endpoint: string,
  data: U
): Promise<T> => {
  const res: AxiosResponse<T> = await api.put(endpoint, data);
  return res.data;
};

export const deleteData = async (endpoint: string): Promise<boolean> => {
  await api.delete(endpoint);
  return true;
};

export const createFormData = async <T>(
  endpoint: string,
  formData: FormData
): Promise<T> => {
  const res: AxiosResponse<T> = await api.post(endpoint, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
  return res.data;
};

export const updateFormData = async <T>(
  endpoint: string,
  formData: FormData
): Promise<T> => {
  const token = localStorage.getItem("accessToken");
  const res: AxiosResponse<T> = await api.put(endpoint, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  });
  return res.data;
};

export default api;
