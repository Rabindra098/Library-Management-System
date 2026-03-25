import React from "react";
import "./App.css";
import { Route, Routes } from "react-router";
import UserLayout from "./Pages/UserLayout/UserLayout";
import Dashboard from "./Pages/Dashbord/Dashboard";
import BookPage from "./Pages/Book/BookPage";
import MyLoans from "./Pages/MyLoans/MyLoans";
import MyReservation from "./Pages/MyReservation/MyReservation";


function App() {
  return (
    <>
      {/* User routes */}
      <Routes>
        <Route element={<UserLayout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/books" element={<BookPage />} />
          <Route path="/my-loans" element={<MyLoans />} />
          <Route path="/my-reservations" element={<MyReservation/>} />
          <Route path="/my-fines" element={<Dashboard />} />
          <Route path="/subscriptions" element={<Dashboard />} />
          <Route path="/wishlist" element={<Dashboard />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
