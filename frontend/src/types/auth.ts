export interface User {
  id: string;
  name: string;
  email: string;
  joinDate: string;
  totalOrders: number;
  pendingOrders: number;
  completedOrders: number;
}

export interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => void;
  isLoading: boolean;
}
