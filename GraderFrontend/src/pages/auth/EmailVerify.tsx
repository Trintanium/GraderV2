import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { createData } from "@/libs/fetchUtils";
import { useUser } from "@/contexts/UserContext"; 
import { UserDto } from "@/types/dto"; 

interface AuthResponse {
  accessToken: string;
  user: UserDto; 
}

export default function VerifyEmail() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { setUser } = useUser();
  const [statusMessage, setStatusMessage] = useState("Verifying email...");

  const token = searchParams.get("token");

  const mutation = useMutation({
    mutationFn: () =>
      createData<AuthResponse, { token: string }>("/auth/email/verify", {
        token: token!,
      }),
    onSuccess: (res: AuthResponse) => {
      localStorage.setItem("accessToken", res.accessToken);
      localStorage.setItem("user", JSON.stringify(res.user));
      setUser(res.user);

      setStatusMessage("Email verified! Redirecting to Home...");
      setTimeout(() => navigate("/"), 1500);
    },
    onError: () =>
      setStatusMessage("Email verification failed. Please try again."),
  });

  useEffect(() => {
    if (token) mutation.mutate();
    else setStatusMessage("Invalid verification link");
  }, [token]);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <p>{statusMessage}</p>
    </div>
  );
}
