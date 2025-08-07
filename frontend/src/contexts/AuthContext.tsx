import React, { createContext, useContext, useState, useEffect } from "react";
import type { User, AuthContextType } from "../types/auth";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Mock users database
  const mockUsers = {
    "sarah@orbit.com": {
      id: "1",
      name: "Sarah Johnson",
      email: "sarah@orbit.com",
      password: "password123",
      joinDate: "January 2023",
      totalOrders: 24,
      pendingOrders: 2,
      completedOrders: 22,
    },
    "demo@orbit.com": {
      id: "2",
      name: "Demo User",
      email: "demo@orbit.com",
      password: "demo",
      joinDate: "December 2023",
      totalOrders: 12,
      pendingOrders: 1,
      completedOrders: 11,
    },
  };

  useEffect(() => {
    // Check for stored auth token
    const storedUser = localStorage.getItem("orbit_user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string): Promise<boolean> => {
    setIsLoading(true);

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000));

    const mockUser = mockUsers[email as keyof typeof mockUsers];
    if (mockUser && mockUser.password === password) {
      const { password: _, ...userWithoutPassword } = mockUser;
      setUser(userWithoutPassword);
      localStorage.setItem("orbit_user", JSON.stringify(userWithoutPassword));
      setIsLoading(false);
      return true;
    }

    setIsLoading(false);
    return false;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("orbit_user");
  };

  const value = {
    user,
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
