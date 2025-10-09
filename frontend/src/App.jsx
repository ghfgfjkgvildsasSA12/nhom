import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import SchoolListPage from './pages/SchoolListPage';
import SchoolCreatePage from './pages/SchoolCreatePage';
import SchoolEditPage from './pages/SchoolEditPage';
import ClassListPage from './pages/ClassListPage';
import ClassCreatePage from './pages/ClassCreatePage';
import ClassEditPage from './pages/ClassEditPage';
import SubjectListPage from './pages/SubjectListPage';
import SubjectCreatePage from './pages/SubjectCreatePage';
import SubjectEditPage from './pages/SubjectEditPage';
import UserListPage from './pages/UserListPage';
import UserCreatePage from './pages/UserCreatePage';
import UserEditPage from './pages/UserEditPage';
import ScheduleListPage from './pages/ScheduleListPage';
import ScheduleCreatePage from './pages/ScheduleCreatePage';
import ScheduleEditPage from './pages/ScheduleEditPage';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<LoginPage />} />
            
            {/* Protected routes */}
            <Route path="/" element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }>
              <Route index element={<Navigate to="/dashboard" replace />} />
              <Route path="dashboard" element={<DashboardPage />} />
              
              {/* School Management */}
              <Route path="schools" element={<SchoolListPage />} />
              <Route path="schools/create" element={<SchoolCreatePage />} />
              <Route path="schools/:id/edit" element={<SchoolEditPage />} />
              
              {/* Class Management */}
              <Route path="classes" element={<ClassListPage />} />
              <Route path="classes/create" element={<ClassCreatePage />} />
              <Route path="classes/:id/edit" element={<ClassEditPage />} />
              
              {/* Subject Management */}
              <Route path="subjects" element={<SubjectListPage />} />
              <Route path="subjects/create" element={<SubjectCreatePage />} />
              <Route path="subjects/:id/edit" element={<SubjectEditPage />} />
              
              {/* User Management */}
              <Route path="users" element={<UserListPage />} />
              <Route path="users/create" element={<UserCreatePage />} />
              <Route path="users/:id/edit" element={<UserEditPage />} />
              
              {/* Schedule Management */}
              <Route path="schedules" element={<ScheduleListPage />} />
              <Route path="schedules/create" element={<ScheduleCreatePage />} />
              <Route path="schedules/:id/edit" element={<ScheduleEditPage />} />
            </Route>
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;