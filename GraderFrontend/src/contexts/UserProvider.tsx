import { useState, useEffect } from "react";
import { UserContext } from "./UserContext";
import { UserDto } from "@/types/dto";

export const UserProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserDto | null>(null);

  // Load user from localStorage on mount
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const userStr = localStorage.getItem("user");

    if (token && userStr) {
      try {
        const parsedUser: UserDto = JSON.parse(userStr);
        setUser(parsedUser);
      } catch {
        localStorage.removeItem("user");
      }
    }
  }, []);

  // Sync user updates to localStorage
  useEffect(() => {
    if (user) {
      localStorage.setItem("user", JSON.stringify(user));
    } else {
      localStorage.removeItem("user");
    }
  }, [user]);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};
