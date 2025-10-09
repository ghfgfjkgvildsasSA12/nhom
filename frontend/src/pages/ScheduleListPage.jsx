import React from 'react';

const ScheduleListPage = () => {
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Schedules</h1>
        <button className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700">
          Add New Schedule
        </button>
      </div>
      
      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <div className="text-center py-12">
          <p className="text-gray-500">Schedule management will be implemented here.</p>
        </div>
      </div>
    </div>
  );
};

export default ScheduleListPage;



