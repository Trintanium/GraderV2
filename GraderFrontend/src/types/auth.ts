import { UserDto } from "@/types/dto";

export interface AuthResponse {
  accessToken: string;
  refreshToken?: string;
  user: UserDto;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface SignUpResponse {
  token: string;
  email: string;
}
