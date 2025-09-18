import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { createData } from "@/libs/fetchUtils";

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const navigate = useNavigate();

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [statusMessage, setStatusMessage] = useState("");
  const [tokenValid, setTokenValid] = useState<boolean | null>(null);

  useEffect(() => {
    if (!token) {
      setStatusMessage("No reset token provided.");
      setTokenValid(false);
      return;
    }

    const verifyToken = async () => {
      try {
        const res = await createData<string, { token: string }>(
          "/auth/password/verify",
          { token }
        );
        setStatusMessage(res);
        setTokenValid(res.includes("valid"));
      } catch {
        setStatusMessage("Error verifying token.");
        setTokenValid(false);
      }
    };

    verifyToken();
  }, [token]);

  const mutation = useMutation({
    mutationFn: () =>
      createData<
        string,
        { token: string; newPassword: string; confirmPassword: string }
      >("/auth/password/reset", {
        token: token!,
        newPassword,
        confirmPassword,
      }),
    onSuccess: () => {
      setStatusMessage("Password reset successfully! Redirecting to login...");
      setTimeout(() => navigate("/login"), 1500);
    },
    onError: () =>
      setStatusMessage("Failed to reset password. Please try again."),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newPassword || !confirmPassword) {
      setStatusMessage("Please fill in both fields");
      return;
    }
    if (newPassword !== confirmPassword) {
      setStatusMessage("Passwords do not match");
      return;
    }
    setStatusMessage("Resetting password...");
    mutation.mutate();
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="w-80 p-6 border rounded-md shadow-md"
    >
      <h1 className="text-3xl mb-6 text-center">Reset Password</h1>

      {tokenValid === null && (
        <div className="text-center text-gray-600">Verifying token...</div>
      )}
      {tokenValid === false && (
        <div className="text-center text-red-600">{statusMessage}</div>
      )}

      {tokenValid && (
        <>
          <input
            type="password"
            placeholder="New Password"
            className="w-full p-2 border rounded mb-4"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <input
            type="password"
            placeholder="Confirm Password"
            className="w-full p-2 border rounded mb-4"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          <button
            type="submit"
            className="bg-sky-800 text-white w-full py-2 rounded-md mt-4"
          >
            Reset Password
          </button>
        </>
      )}

      {statusMessage && (
        <div className="text-center mt-4 text-red-600">{statusMessage}</div>
      )}
    </form>
  );
}
