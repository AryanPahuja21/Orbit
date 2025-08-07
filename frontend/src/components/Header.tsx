import React from "react";
import { Bell, User, Orbit, LogOut, Sun, Moon } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { useTheme } from "../contexts/ThemeContext";

interface HeaderProps {
  activeView: "tracking" | "notifications" | "profile";
  onViewChange: (view: "tracking" | "notifications" | "profile") => void;
  notificationCount: number;
}

const Header: React.FC<HeaderProps> = ({
  activeView,
  onViewChange,
  notificationCount,
}) => {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="shadow-sm border-b border-gray-200 dark:border-slate-700 sticky top-0 z-50 backdrop-blur-sm bg-white/95 dark:bg-slate-900/95">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-gradient-to-r from-violet-600 to-indigo-600 rounded-lg flex items-center justify-center">
              <Orbit className="h-5 w-5 text-white" />
            </div>
            <h1 className="text-xl font-bold text-gray-900 dark:text-white">
              Orbit
            </h1>
          </div>

          <nav className="hidden md:flex space-x-8">
            <button
              onClick={() => onViewChange("tracking")}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                activeView === "tracking"
                  ? "text-violet-600 dark:text-violet-400 bg-violet-50 dark:bg-violet-500/10"
                  : "text-gray-500 dark:text-slate-400 hover:text-gray-700 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800"
              }`}
            >
              Track Order
            </button>
            <button
              onClick={() => onViewChange("profile")}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                activeView === "profile"
                  ? "text-violet-600 dark:text-violet-400 bg-violet-50 dark:bg-violet-500/10"
                  : "text-gray-500 dark:text-slate-400 hover:text-gray-700 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800"
              }`}
            >
              My Orders
            </button>
          </nav>

          <div className="flex items-center space-x-4">
            {/* Theme Toggle */}
            <button
              onClick={toggleTheme}
              className="p-2 rounded-lg text-gray-400 dark:text-slate-400 hover:text-gray-500 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800 transition-all duration-200"
            >
              {theme === "light" ? (
                <Moon className="h-5 w-5" />
              ) : (
                <Sun className="h-5 w-5" />
              )}
            </button>

            {/* Notifications */}
            <button
              onClick={() => onViewChange("notifications")}
              className="relative p-2 rounded-lg text-gray-400 dark:text-slate-400 hover:text-gray-500 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800 transition-all duration-200"
            >
              <Bell className="h-6 w-6" />
              {notificationCount > 0 && (
                <span className="absolute -top-1 -right-1 h-5 w-5 bg-gradient-to-r from-red-500 to-pink-500 text-white text-xs rounded-full flex items-center justify-center animate-pulse">
                  {notificationCount}
                </span>
              )}
            </button>

            {/* User Menu */}
            <div className="relative group">
              <button className="flex items-center space-x-2 p-2 rounded-lg text-gray-400 dark:text-slate-400 hover:text-gray-500 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800 transition-all duration-200">
                <User className="h-6 w-6" />
                <span className="hidden sm:block text-sm font-medium text-gray-700 dark:text-slate-300">
                  {user?.name}
                </span>
              </button>

              {/* Dropdown Menu */}
              <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-lg shadow-lg border border-gray-200 dark:border-slate-700 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 transform translate-y-1 group-hover:translate-y-0">
                <div className="p-3 border-b border-gray-200 dark:border-slate-700">
                  <p className="text-sm font-medium text-gray-900 dark:text-white">
                    {user?.name}
                  </p>
                  <p className="text-xs text-gray-500 dark:text-slate-400">
                    {user?.email}
                  </p>
                </div>
                <button
                  onClick={() => onViewChange("profile")}
                  className="w-full text-left px-3 py-2 text-sm text-gray-700 dark:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-700 transition-colors flex items-center gap-2"
                >
                  <User className="h-4 w-4" />
                  Profile
                </button>
                <button
                  onClick={logout}
                  className="w-full text-left px-3 py-2 text-sm text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-500/10 transition-colors flex items-center gap-2 rounded-b-lg"
                >
                  <LogOut className="h-4 w-4" />
                  Sign Out
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Mobile Navigation */}
        <div className="md:hidden border-t border-gray-200 dark:border-slate-700 pt-2 pb-2">
          <div className="flex space-x-1">
            <button
              onClick={() => onViewChange("profile")}
              className={`flex-1 px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                activeView === "tracking"
                  ? "text-violet-600 dark:text-violet-400 bg-violet-50 dark:bg-violet-500/10"
                  : "text-gray-500 dark:text-slate-400 hover:text-gray-700 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800"
              }`}
            >
              Track Order
            </button>
            <button
              onClick={() => onViewChange("profile")}
              className={`flex-1 px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 ${
                activeView === "profile"
                  ? "text-violet-600 dark:text-violet-400 bg-violet-50 dark:bg-violet-500/10"
                  : "text-gray-500 dark:text-slate-400 hover:text-gray-700 dark:hover:text-slate-300 hover:bg-gray-50 dark:hover:bg-slate-800"
              }`}
            >
              My Orders
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
