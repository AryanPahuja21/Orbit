import React, { useState } from "react";
import { Search, Package, Truck, CheckCircle, Clock } from "lucide-react";
// import { useAuth } from "../contexts/AuthContext";

interface OrderStatus {
  id: string;
  status: "pending" | "processing" | "shipped" | "delivered";
  items: Array<{
    name: string;
    quantity: number;
    price: number;
  }>;
  total: number;
  estimatedDelivery: string;
  trackingSteps: Array<{
    status: string;
    timestamp: string;
    location?: string;
    completed: boolean;
  }>;
}

const OrderTracker: React.FC = () => {
  //   const { user } = useAuth();
  const [orderId, setOrderId] = useState("");
  const [orderData, setOrderData] = useState<OrderStatus | null>(null);
  const [loading, setLoading] = useState(false);

  // Mock data for demonstration
  const mockOrders: { [key: string]: OrderStatus } = {
    "TR-2024-001": {
      id: "TR-2024-001",
      status: "shipped",
      items: [
        { name: "Wireless Headphones", quantity: 1, price: 199.99 },
        { name: "Phone Case", quantity: 2, price: 24.99 },
      ],
      total: 249.97,
      estimatedDelivery: "2024-01-15",
      trackingSteps: [
        {
          status: "Order Placed",
          timestamp: "2024-01-10 10:30 AM",
          completed: true,
        },
        {
          status: "Processing",
          timestamp: "2024-01-10 2:15 PM",
          completed: true,
        },
        {
          status: "Shipped",
          timestamp: "2024-01-12 9:00 AM",
          location: "New York, NY",
          completed: true,
        },
        { status: "Out for Delivery", timestamp: "", completed: false },
        { status: "Delivered", timestamp: "", completed: false },
      ],
    },
    "TR-2024-002": {
      id: "TR-2024-002",
      status: "processing",
      items: [{ name: "Gaming Mouse", quantity: 1, price: 79.99 }],
      total: 79.99,
      estimatedDelivery: "2024-01-18",
      trackingSteps: [
        {
          status: "Order Placed",
          timestamp: "2024-01-11 3:45 PM",
          completed: true,
        },
        {
          status: "Processing",
          timestamp: "2024-01-12 11:20 AM",
          completed: true,
        },
        { status: "Shipped", timestamp: "", completed: false },
        { status: "Out for Delivery", timestamp: "", completed: false },
        { status: "Delivered", timestamp: "", completed: false },
      ],
    },
  };

  const handleTrackOrder = async () => {
    if (!orderId.trim()) return;

    setLoading(true);
    // Simulate API call
    setTimeout(() => {
      const order = mockOrders[orderId.toUpperCase()];
      setOrderData(order || null);
      setLoading(false);
    }, 1000);
  };

  const getStatusIcon = (status: string) => {
    switch (status.toLowerCase()) {
      case "order placed":
        return <Package className="h-5 w-5" />;
      case "processing":
        return <Clock className="h-5 w-5" />;
      case "shipped":
      case "out for delivery":
        return <Truck className="h-5 w-5" />;
      case "delivered":
        return <CheckCircle className="h-5 w-5" />;
      default:
        return <Clock className="h-5 w-5" />;
    }
  };

  const getStatusColor = (completed: boolean, isActive: boolean) => {
    if (completed) return "text-emerald-600 bg-emerald-100";
    if (isActive) return "text-blue-600 bg-blue-100";
    return "text-gray-400 bg-gray-100";
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
          Track Your Order
        </h2>
        <p className="text-gray-600 dark:text-slate-400">
          Enter your order ID to get real-time tracking updates
        </p>
      </div>

      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-gray-200 dark:border-slate-700 p-6 mb-8">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Enter Order ID (e.g., TR-2024-001)"
              value={orderId}
              onChange={(e) => setOrderId(e.target.value)}
              className="w-full px-4 py-3 border border-gray-300 dark:border-slate-600 rounded-lg bg-white dark:bg-slate-700 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-slate-400 focus:ring-2 focus:ring-violet-500 focus:border-transparent transition-all duration-200"
              onKeyPress={(e) => e.key === "Enter" && handleTrackOrder()}
            />
          </div>
          <button
            onClick={handleTrackOrder}
            disabled={loading || !orderId.trim()}
            className="px-6 py-3 bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-700 hover:to-indigo-700 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 flex items-center justify-center gap-2 transform hover:scale-[1.02] active:scale-[0.98]"
          >
            <Search className="h-5 w-5" />
            {loading ? "Tracking..." : "Track Order"}
          </button>
        </div>
      </div>

      {orderData && (
        <div className="space-y-6">
          {/* Order Summary */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-gray-200 dark:border-slate-700 p-6">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4">
              <div>
                <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                  Order {orderData.id}
                </h3>
                <p className="text-gray-600 dark:text-slate-400">
                  Estimated delivery: {orderData.estimatedDelivery}
                </p>
              </div>
              <div className="mt-2 sm:mt-0">
                <span
                  className={`px-3 py-1 rounded-full text-sm font-medium ${
                    orderData.status === "delivered"
                      ? "bg-emerald-100 dark:bg-emerald-500/20 text-emerald-800 dark:text-emerald-400"
                      : orderData.status === "shipped"
                      ? "bg-blue-100 dark:bg-blue-500/20 text-blue-800 dark:text-blue-400"
                      : orderData.status === "processing"
                      ? "bg-amber-100 dark:bg-amber-500/20 text-amber-800 dark:text-amber-400"
                      : "bg-gray-100 dark:bg-slate-500/20 text-gray-800 dark:text-slate-400"
                  }`}
                >
                  {orderData.status.charAt(0).toUpperCase() +
                    orderData.status.slice(1)}
                </span>
              </div>
            </div>

            <div className="border-t border-gray-200 dark:border-slate-700 pt-4">
              <h4 className="font-medium text-gray-900 dark:text-white mb-3">
                Items
              </h4>
              <div className="space-y-2">
                {orderData.items.map((item, index) => (
                  <div key={index} className="flex justify-between text-sm">
                    <span className="text-gray-600 dark:text-slate-400">
                      {item.name} × {item.quantity}
                    </span>
                    <span className="font-medium text-gray-900 dark:text-white">
                      ${item.price.toFixed(2)}
                    </span>
                  </div>
                ))}
              </div>
              <div className="border-t border-gray-200 dark:border-slate-700 mt-3 pt-3 flex justify-between font-semibold text-gray-900 dark:text-white">
                <span>Total</span>
                <span>${orderData.total.toFixed(2)}</span>
              </div>
            </div>
          </div>

          {/* Tracking Timeline */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-gray-200 dark:border-slate-700 p-6">
            <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-6">
              Tracking Timeline
            </h3>
            <div className="space-y-4">
              {orderData.trackingSteps.map((step, index) => {
                const isActive =
                  !step.completed &&
                  index > 0 &&
                  orderData.trackingSteps[index - 1].completed;
                return (
                  <div key={index} className="flex items-start gap-4">
                    <div
                      className={`p-2 rounded-full ${getStatusColor(
                        step.completed,
                        isActive
                      )} transition-all duration-200`}
                    >
                      {getStatusIcon(step.status)}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p
                        className={`font-medium ${
                          step.completed
                            ? "text-gray-900 dark:text-white"
                            : "text-gray-500 dark:text-slate-400"
                        }`}
                      >
                        {step.status}
                      </p>
                      {step.timestamp && (
                        <p className="text-sm text-gray-500 dark:text-slate-400">
                          {step.timestamp}
                        </p>
                      )}
                      {step.location && (
                        <p className="text-sm text-gray-500 dark:text-slate-400">
                          {step.location}
                        </p>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      )}

      {orderId && !orderData && !loading && (
        <div className="text-center py-8">
          <div className="bg-red-50 dark:bg-red-500/10 border border-red-200 dark:border-red-500/20 rounded-xl p-6">
            <p className="text-red-800 dark:text-red-400">
              Order not found. Please check your order ID and try again.
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderTracker;
