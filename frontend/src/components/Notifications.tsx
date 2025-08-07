import React from "react";
import { Bell, Package, Truck, CheckCircle, X } from "lucide-react";

interface Notification {
  id: string;
  type: "order_placed" | "shipped" | "delivered" | "delay";
  title: string;
  message: string;
  timestamp: string;
  orderId: string;
  read: boolean;
}

interface NotificationsProps {
  onMarkAsRead: (id: string) => void;
  onDelete: (id: string) => void;
}

const Notifications: React.FC<NotificationsProps> = ({
  onMarkAsRead,
  onDelete,
}) => {
  // Mock notifications data
  const notifications: Notification[] = [
    {
      id: "1",
      type: "shipped",
      title: "Order Shipped",
      message: "Your order TR-2024-001 has been shipped and is on its way!",
      timestamp: "2 hours ago",
      orderId: "TR-2024-001",
      read: false,
    },
    {
      id: "2",
      type: "order_placed",
      title: "Order Confirmed",
      message:
        "Your order TR-2024-002 has been confirmed and is being processed.",
      timestamp: "1 day ago",
      orderId: "TR-2024-002",
      read: false,
    },
    {
      id: "3",
      type: "delivered",
      title: "Order Delivered",
      message: "Your order TR-2024-003 has been successfully delivered.",
      timestamp: "3 days ago",
      orderId: "TR-2024-003",
      read: true,
    },
    {
      id: "4",
      type: "delay",
      title: "Delivery Delayed",
      message:
        "Your order TR-2023-145 delivery has been delayed due to weather conditions.",
      timestamp: "1 week ago",
      orderId: "TR-2023-145",
      read: true,
    },
  ];

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case "order_placed":
        return <Package className="h-6 w-6 text-blue-600" />;
      case "shipped":
        return <Truck className="h-6 w-6 text-amber-600" />;
      case "delivered":
        return <CheckCircle className="h-6 w-6 text-emerald-600" />;
      case "delay":
        return <Bell className="h-6 w-6 text-red-600" />;
      default:
        return <Bell className="h-6 w-6 text-gray-600" />;
    }
  };

  const unreadCount = notifications.filter((n) => !n.read).length;

  return (
    <div className="max-w-4xl mx-auto">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-3xl font-bold text-gray-900 dark:text-white">
            Notifications
          </h2>
          <p className="text-gray-600 dark:text-slate-400 mt-1">
            {unreadCount > 0
              ? `${unreadCount} unread notification${
                  unreadCount > 1 ? "s" : ""
                }`
              : "All caught up!"}
          </p>
        </div>
      </div>

      <div className="space-y-4">
        {notifications.length === 0 ? (
          <div className="text-center py-12">
            <Bell className="h-12 w-12 text-gray-400 dark:text-slate-500 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
              No notifications yet
            </h3>
            <p className="text-gray-500 dark:text-slate-400">
              We'll notify you when there are updates on your orders.
            </p>
          </div>
        ) : (
          notifications.map((notification) => (
            <div
              key={notification.id}
              className={`bg-white dark:bg-slate-800 rounded-xl border transition-all duration-200 hover:shadow-md ${
                notification.read
                  ? "border-gray-200 dark:border-slate-700 opacity-75"
                  : "border-violet-200 dark:border-violet-500/30 shadow-sm"
              }`}
            >
              <div className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-start space-x-4 flex-1">
                    <div className="flex-shrink-0">
                      {getNotificationIcon(notification.type)}
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1">
                        <h3
                          className={`font-semibold ${
                            notification.read
                              ? "text-gray-700 dark:text-slate-300"
                              : "text-gray-900 dark:text-white"
                          }`}
                        >
                          {notification.title}
                        </h3>
                        {!notification.read && (
                          <div className="w-2 h-2 bg-violet-600 rounded-full animate-pulse"></div>
                        )}
                      </div>
                      <p
                        className={`text-sm mb-2 ${
                          notification.read
                            ? "text-gray-500 dark:text-slate-400"
                            : "text-gray-700 dark:text-slate-300"
                        }`}
                      >
                        {notification.message}
                      </p>
                      <div className="flex items-center gap-4 text-xs text-gray-500 dark:text-slate-400">
                        <span>{notification.timestamp}</span>
                        <span className="px-2 py-1 bg-gray-100 dark:bg-slate-700 rounded text-gray-700 dark:text-slate-300">
                          Order {notification.orderId}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2 ml-4">
                    {!notification.read && (
                      <button
                        onClick={() => onMarkAsRead(notification.id)}
                        className="px-3 py-1 text-xs font-medium text-violet-600 dark:text-violet-400 hover:text-violet-700 dark:hover:text-violet-300 hover:bg-violet-50 dark:hover:bg-violet-500/10 rounded transition-colors"
                      >
                        Mark as read
                      </button>
                    )}
                    <button
                      onClick={() => onDelete(notification.id)}
                      className="p-1 text-gray-400 dark:text-slate-500 hover:text-gray-600 dark:hover:text-slate-400 hover:bg-gray-100 dark:hover:bg-slate-700 rounded transition-colors"
                    >
                      <X className="h-4 w-4" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Notifications;
