import React, { useState, useEffect } from "react";
import { AuthProvider, useAuth } from "./contexts/AuthContext";
import { ThemeProvider } from "./contexts/ThemeContext";
import LoginForm from "./components/LoginForm";
import Header from "./components/Header";
import OrderTracker from "./components/OrderTracker";
// import Notifications from "./components/Notifications";
import UserProfile from "./components/UserProfile";

const AppContent: React.FC = () => {
  const { user } = useAuth();
  const [activeView, setActiveView] = useState<
    "tracking" | "notifications" | "profile"
  >("tracking");
  const [notificationCount, setNotificationCount] = useState(2);

  // Simulate receiving push notifications
  useEffect(() => {
    const interval = setInterval(() => {
      // Simulate random notification updates
      if (Math.random() > 0.8) {
        setNotificationCount((prev) => prev + 1);
      }
    }, 30000); // Check every 30 seconds

    return () => clearInterval(interval);
  }, []);

  // const handleMarkAsRead = (notificationId: string) => {
  //   setNotificationCount((prev) => Math.max(0, prev - 1));
  // };

  // const handleDeleteNotification = (notificationId: string) => {
  //   setNotificationCount((prev) => Math.max(0, prev - 1));
  // };

  if (!user) {
    return <LoginForm />;
  }

  const renderActiveView = () => {
    switch (activeView) {
      case "tracking":
        return <OrderTracker />;
      case "notifications":
        return (
          // <Notifications
          //   onMarkAsRead={handleMarkAsRead}
          //   onDelete={handleDeleteNotification}
          // />
          <></>
        );
      case "profile":
        return <UserProfile />;
      default:
        return <OrderTracker />;
    }
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-50 dark:bg-slate-900 transition-colors duration-200">
      <Header
        activeView={activeView}
        onViewChange={setActiveView}
        notificationCount={notificationCount}
      />

      <main className="flex-grow py-8 px-4 sm:px-6 lg:px-8 transition-colors duration-200">
        {renderActiveView()}
      </main>

      <footer className="mt-auto bg-white dark:bg-slate-800 border-t border-gray-200 dark:border-slate-700 transition-colors duration-200">
        <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
          <div className="text-center text-gray-500 dark:text-slate-400 text-sm">
            <p>&copy; 2025 Orbit. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
