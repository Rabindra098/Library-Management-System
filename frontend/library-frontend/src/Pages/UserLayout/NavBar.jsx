import React from "react";
import {
  AppBar,
  IconButton,
  Toolbar,
  Typography,
  Box,
  Tooltip,
  Avatar,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { navigationItems } from "./NavigationItems";
import { isActive } from "./util";
import { useLocation } from "react-router";
import SearchIcon from "@mui/icons-material/Search";
import { Notifications } from "@mui/icons-material";
import ContrastIcon from "@mui/icons-material/Contrast";

const drawerWidth = 240;

const user = {
  fullName: "John Doe",
  profilePicture: "https://example.com/profile.jpg",
};

const Navbar = ({ handleDrawerToggle }) => {
  const location = useLocation();

  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        width: { md: `calc(100% - ${drawerWidth}px)` },
        ml: { md: `${drawerWidth}px` },
        bgcolor: "white",
        color: "text.primary",
        borderBottom: "1px solid rgba(0,0,0,0.06)",
        zIndex: (theme) => theme.zIndex.drawer + 1,
      }}
    >
      <Toolbar
        sx={{
          minHeight: 64,
          px: 3,
          display: "flex",
          alignItems: "center",
        }}
      >
        {/* Mobile menu icon */}
        <IconButton
          color="inherit"
          edge="start"
          onClick={handleDrawerToggle}
          sx={{ mr: 2, display: { md: "none" } }}
        >
          <MenuIcon />
        </IconButton>

        {/* Page title */}
        <Typography
          variant="h6"
          noWrap
          sx={{ fontWeight: 600 }}
        >
          {navigationItems.find((item) =>
            isActive(item.path, location)
          )?.title || "Dashboard"}
        </Typography>

        {/* Spacer pushes icons to right */}
        <Box sx={{ flexGrow: 1 }} />

        {/* Right-side actions */}
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <Tooltip title="Search">
            <IconButton>
              <SearchIcon />
            </IconButton>
          </Tooltip>

          <Tooltip title="Notifications">
            <IconButton>
              <Notifications />
            </IconButton>
          </Tooltip>

          <Tooltip title="Theme">
            <IconButton>
              <ContrastIcon />
            </IconButton>
          </Tooltip>

          <Tooltip title="Account">
            <IconButton>
              <Avatar
                src={user?.profilePicture}
                sx={{ width: 36, height: 36 }}
              >
                {user?.fullName?.charAt(0)}
              </Avatar>
            </IconButton>
          </Tooltip>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
