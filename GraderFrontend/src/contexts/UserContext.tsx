import { createContext, useContext } from "react";

import { UserDto } from "@/types/dto";
export interface UserContextType {
  user: UserDto | null;
  setUser: (user: UserDto | null) => void;
}

export const UserContext = createContext<UserContextType | undefined>(
  undefined
);

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within a UserProvider");
  return context;
};
