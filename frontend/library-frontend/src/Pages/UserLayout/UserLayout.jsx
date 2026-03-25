import React from "react";
import { Box, Toolbar } from "@mui/material";

import UserSideBar from "./UserSideBar";
import { Outlet } from "react-router";
import Navbar from "./NavBar";

const drawerWidth = 240;

const UserLayout = () => {
  return (
    <Box
      sx={{
        display: "flex",
        minHeight: "100vh",
        bgcolor: "white"
      }}
    >
      {/* User Sidebar */}
      <UserSideBar />

      {/* Main Content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { md: `calc(100% - ${drawerWidth}px)` },
          minHeight: "100vh",
        }}
      >
        {/* Navbar */}
        <Navbar />
        <Toolbar />
        <Box>
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default UserLayout;
