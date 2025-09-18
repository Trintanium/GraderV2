import axios from "axios";
import type { AxiosResponse } from "axios";
import type { InternalAxiosRequestConfig } from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

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
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return res.data;
};

// Update FormData PUT request
export const updateFormData = async <T>(
  endpoint: string,
  formData: FormData
): Promise<T> => {
  const res: AxiosResponse<T> = await api.put(endpoint, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return res.data;
};

export default api;
