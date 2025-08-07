import React, { useState } from "react";
import { User, Package, Calendar, Eye, Filter } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";

interface Order {
  id: string;
  date: string;
  status: "pending" | "processing" | "shipped" | "delivered" | "cancelled";
  total: number;
  items: number;
  estimatedDelivery?: string;
}

const UserProfile: React.FC = () => {
  const { user: authUser } = useAuth();
  //   const [activeTab, setActiveTab] = useState<"overview" | "orders">("overview");
  const [statusFilter, setStatusFilter] = useState<string>("all");

  // Mock user data
  const user = authUser || {
    name: "Demo User",
    email: "demo@orbit.com",
    joinDate: "January 2024",
    totalOrders: 12,
    pendingOrders: 1,
    completedOrders: 11,
  };

  // Mock orders data
  const orders: Order[] = [
    {
      id: "TR-2024-001",
      date: "2024-01-10",
      status: "shipped",
      total: 249.97,
      items: 2,
      estimatedDelivery: "2024-01-15",
    },
    {
      id: "TR-2024-002",
      date: "2024-01-11",
      status: "processing",
      total: 79.99,
      items: 1,
      estimatedDelivery: "2024-01-18",
    },
    {
      id: "TR-2023-145",
      date: "2023-12-20",
      status: "delivered",
      total: 399.99,
      items: 3,
    },
    {
      id: "TR-2023-144",
      date: "2023-12-15",
      status: "delivered",
      total: 159.98,
      items: 2,
    },
    {
      id: "TR-2023-143",
      date: "2023-12-01",
      status: "delivered",
      total: 89.99,
      items: 1,
    },
  ];

  const filteredOrders =
    statusFilter === "all"
      ? orders
      : orders.filter((order) => order.status === statusFilter);

  const getStatusColor = (status: string) => {
    switch (status) {
      case "delivered":
        return "bg-emerald-100 dark:bg-emerald-500/20 text-emerald-800 dark:text-emerald-400";
      case "shipped":
        return "bg-blue-100 dark:bg-blue-500/20 text-blue-800 dark:text-blue-400";
      case "processing":
        return "bg-amber-100 dark:bg-amber-500/20 text-amber-800 dark:text-amber-400";
      case "pending":
        return "bg-gray-100 dark:bg-slate-500/20 text-gray-800 dark:text-slate-400";
      case "cancelled":
        return "bg-red-100 dark:bg-red-500/20 text-red-800 dark:text-red-400";
      default:
        return "bg-gray-100 dark:bg-slate-500/20 text-gray-800 dark:text-slate-400";
    }
  };

  return (
    <div className="max-w-6xl mx-auto">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-gray-200 dark:border-slate-700 mb-8">
        <div className="p-6 border-b border-gray-200 dark:border-slate-700">
          <div className="flex items-center space-x-4">
            <div className="w-16 h-16 bg-gradient-to-r from-violet-600 to-indigo-600 rounded-full flex items-center justify-center">
              <User className="h-8 w-8 text-white" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
                {user.name}
              </h1>
              <p className="text-gray-600 dark:text-slate-400">{user.email}</p>
              <p className="text-sm text-gray-500 dark:text-slate-400">
                Member since {user.joinDate}
              </p>
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center">
              <div className="text-2xl font-bold text-gray-900 dark:text-white">
                {user.totalOrders}
              </div>
              <div className="text-gray-600 dark:text-slate-400">
                Total Orders
              </div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-amber-600 dark:text-amber-400">
                {user.pendingOrders}
              </div>
              <div className="text-gray-600 dark:text-slate-400">
                Pending Orders
              </div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-emerald-600 dark:text-emerald-400">
                {user.completedOrders}
              </div>
              <div className="text-gray-600 dark:text-slate-400">
                Completed Orders
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Orders Section */}
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-gray-200 dark:border-slate-700">
        <div className="p-6 border-b border-gray-200 dark:border-slate-700">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <h2 className="text-xl font-semibold text-gray-900 dark:text-white flex items-center gap-2">
              <Package className="h-5 w-5" />
              Order History
            </h2>

            <div className="flex items-center gap-3">
              <div className="flex items-center gap-2">
                <Filter className="h-4 w-4 text-gray-500 dark:text-slate-400" />
                <select
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                  className="border border-gray-300 dark:border-slate-600 rounded-lg px-3 py-1 text-sm bg-white dark:bg-slate-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-violet-500 focus:border-transparent transition-all duration-200"
                >
                  <option value="all">All Status</option>
                  <option value="pending">Pending</option>
                  <option value="processing">Processing</option>
                  <option value="shipped">Shipped</option>
                  <option value="delivered">Delivered</option>
                  <option value="cancelled">Cancelled</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <div className="divide-y divide-gray-200 dark:divide-slate-700">
          {filteredOrders.map((order) => (
            <div
              key={order.id}
              className="p-6 hover:bg-gray-50 dark:hover:bg-slate-700/50 transition-colors"
            >
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="font-semibold text-gray-900 dark:text-white">
                      {order.id}
                    </h3>
                    <span
                      className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                        order.status
                      )}`}
                    >
                      {order.status.charAt(0).toUpperCase() +
                        order.status.slice(1)}
                    </span>
                  </div>
                  <div className="flex items-center gap-4 text-sm text-gray-600 dark:text-slate-400">
                    <span className="flex items-center gap-1">
                      <Calendar className="h-4 w-4" />
                      {new Date(order.date).toLocaleDateString()}
                    </span>
                    <span>
                      {order.items} item{order.items > 1 ? "s" : ""}
                    </span>
                    {order.estimatedDelivery && (
                      <span>
                        Est. delivery:{" "}
                        {new Date(order.estimatedDelivery).toLocaleDateString()}
                      </span>
                    )}
                  </div>
                </div>

                <div className="flex items-center gap-4">
                  <div className="text-right">
                    <div className="font-semibold text-gray-900 dark:text-white">
                      ${order.total.toFixed(2)}
                    </div>
                  </div>
                  <button className="px-3 py-1 text-sm text-violet-600 dark:text-violet-400 hover:text-violet-700 dark:hover:text-violet-300 hover:bg-violet-50 dark:hover:bg-violet-500/10 rounded-lg transition-colors flex items-center gap-1">
                    <Eye className="h-4 w-4" />
                    View Details
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {filteredOrders.length === 0 && (
          <div className="text-center py-12">
            <Package className="h-12 w-12 text-gray-400 dark:text-slate-500 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
              No orders found
            </h3>
            <p className="text-gray-500 dark:text-slate-400">
              {statusFilter === "all"
                ? "You haven't placed any orders yet."
                : `No orders with status "${statusFilter}" found.`}
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default UserProfile;
