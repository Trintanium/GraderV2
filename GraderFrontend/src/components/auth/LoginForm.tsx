import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { createData } from "@/libs/fetchUtils";
import { useUser } from "@/contexts/UserContext";
import { AuthResponse } from "@/types/auth";

export default function LoginForm() {
  const navigate = useNavigate();
  const { setUser } = useUser();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [statusMessage, setStatusMessage] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const mutation = useMutation<
    AuthResponse,
    { message: string },
    { email: string; password: string }
  >({
    mutationFn: (data) =>
      createData<AuthResponse, { email: string; password: string }>(
        "/auth/signIn",
        data
      ),
    onSuccess: (response) => {
      const token = response.accessToken;
      const user = response.user;

      localStorage.setItem("accessToken", token);
      localStorage.setItem("user", JSON.stringify(user));

      setUser(user);

      setStatusMessage("Login successful! Redirecting to Home...");
      setTimeout(() => navigate("/"), 2000);
    },
    onError: (err) => {
      setStatusMessage(err?.message || "Login failed. Check your credentials.");
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (email && password) {
      const fullEmail = email.includes("@gmail.com")
        ? email
        : `${email}@gmail.com`;
      setStatusMessage("Logging in...");
      mutation.mutate({ email: fullEmail, password });
    } else {
      setStatusMessage("Please enter both email and password.");
    }
  };

  return (
    <div className="w-full h-full flex justify-center items-center">
      <form onSubmit={handleSubmit} className="w-full h-3/5">
        <h1 className="text-5xl mb-6 text-center">Login</h1>
        <div>Email</div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type="text"
            className="w-full p-2 outline-none"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <span className="px-2 text-gray-500">@gmail.com</span>
        </div>
        <div>Password</div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type={showPassword ? "text" : "password"}
            className="w-full p-2 outline-none"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <span
            className="px-2  cursor-pointer text-gray-500"
            onClick={() => setShowPassword(!showPassword)}
          >
            <i className={`fas ${showPassword ? "fa-eye" : "fa-eye-slash"}`} />
          </span>
        </div>
        <div className="flex justify-end items-center mb-4">
          <a href="/password/form" className="text-white hover:text-gray-400">
            Forgot Password
          </a>
        </div>
        <button
          type="submit"
          className={`w-full py-2 rounded-md text-white ${
            mutation.isPending ? "bg-gray-400" : "bg-sky-800"
          }`}
          disabled={mutation.isPending}
        >
          {mutation.isPending ? "Logging in..." : "Login"}
        </button>
        {statusMessage && (
          <div
            className={`mb-2 text-center ${
              mutation.isPending ? "text-blue-500" : "text-red-500"
            }`}
          >
            {statusMessage}
          </div>
        )}
      </form>
    </div>
  );
}
