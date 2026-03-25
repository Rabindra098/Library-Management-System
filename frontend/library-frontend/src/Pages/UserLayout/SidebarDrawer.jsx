import React from "react";
import {
  alpha,
  Avatar,
  Box,
  Divider,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  Tooltip,
  Typography,
} from "@mui/material";
import { MenuBook, Logout } from "@mui/icons-material";
import { navigationItems, secondaryItems } from "./NavigationItems";
import { useLocation, useNavigate } from "react-router";
import { isActive } from "./util";

const SidebarDrawer = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleChangePath = (path) => {
    navigate(path);
  };

  const handleLogout = () => {
    console.log("Logout");
  };

  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        background: "linear-gradient(180deg, #1e293b 0%, #0f172a 100%)",
        color: "white",
      }}
    >
      {/* ===== Header ===== */}
      <Box sx={{ p: 3, display: "flex", alignItems: "center", gap: 2 }}>
        <Avatar
          sx={{
            width: 44,
            height: 44,
            background: "linear-gradient(135deg, #667eea, #764ba2)",
          }}
        >
          <MenuBook />
        </Avatar>

        <Box>
          <Typography fontWeight={600}>RZBBook</Typography>
          <Typography variant="caption" sx={{ opacity: 0.6, letterSpacing: 1 }}>
            LIBRARY HUB
          </Typography>
        </Box>
      </Box>

      {/* ===== Menu (Scrollable Area) ===== */}
      <Box sx={{ flexGrow: 1, overflowY: "auto" }}>
        <List sx={{ px: 2 }}>
          {navigationItems.map((item, index) => {
            const active = isActive(item.path, location);

            return (
              <ListItem key={index} disablePadding sx={{ mb: 0.5 }}>
                <Tooltip
                  title={item.description}
                  arrow
                  placement="right"
                  componentsProps={{
                    tooltip: {
                      sx: {
                        bgcolor: "rgba(15, 23, 42, 0.75)",
                        backdropFilter: "blur(10px)",
                        color: "#f8fafc",
                        fontSize: "0.75rem",
                        px: 1.5,
                        py: 0.75,
                        borderRadius: 2,
                        border: "1px solid rgba(255,255,255,0.1)",
                      },
                    },
                    arrow: {
                      sx: { color: "rgba(15, 23, 42, 0.75)" },
                    },
                  }}
                >
                  <ListItemButton
                    onClick={() => handleChangePath(item.path)}
                    sx={{
                      borderRadius: 2.5,
                      py: 1.5,
                      px: 2,
                      transition: "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)",
                      position: "relative",
                      bgcolor: active
                        ? "linear-gradient(135deg, rgba(99, 102, 241, 0.25) 0%, rgba(168, 85, 247, 0.25) 100%)"
                        : "transparent",
                      border: active
                        ? "1px solid rgba(99, 102, 241, 0.3)"
                        : "1px solid transparent",
                      backdropFilter: active ? "blur(10px)" : "none",
                      "&:hover": {
                        bgcolor: active
                          ? alpha("#6366f1", 0.3)
                          : "rgba(255,255,255,0.05)",
                        transform: "translateX(6px)",
                        border: "1px solid rgba(255,255,255,0.08)",
                      },
                      "&::before": active
                        ? {
                            content: '""',
                            position: "absolute",
                            left: 0,
                            top: "50%",
                            transform: "translateY(-50%)",
                            width: 4,
                            height: "70%",
                            borderRadius: "0 4px 4px 0",
                            background:
                              "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                            boxShadow:
                              "0 0 12px rgba(102, 126, 234, 0.6)",
                          }
                        : {},
                    }}
                  >
                    <ListItemIcon
                      sx={{
                        minWidth: 40,
                        color: active
                          ? "#a5b4fc"
                          : "rgba(255,255,255,0.7)",
                      }}
                    >
                      {item.icon}
                    </ListItemIcon>

                    <Typography
                      sx={{
                        fontSize: "0.95rem",
                        fontWeight: active ? 600 : 400,
                      }}
                    >
                      {item.title}
                    </Typography>
                  </ListItemButton>
                </Tooltip>
              </ListItem>
            );
          })}

          <Divider
            sx={{ my: 2, borderColor: "rgba(255,255,255,0.08)" }}
          />

          {secondaryItems.map((item, index) => {
            const active = isActive(item.path, location);

            return (
              <ListItem key={index} disablePadding>
                <ListItemButton
                  onClick={() => handleChangePath(item.path)}
                  sx={{
                    borderRadius: 2.5,
                    py: 1.5,
                    px: 2,
                    transition: "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)",
                    position: "relative",
                    bgcolor: active
                      ? "linear-gradient(135deg, rgba(99, 102, 241, 0.25) 0%, rgba(168, 85, 247, 0.25) 100%)"
                      : "transparent",
                    border: active
                      ? "1px solid rgba(99, 102, 241, 0.3)"
                      : "1px solid transparent",
                    "&:hover": {
                      bgcolor: active
                        ? alpha("#6366f1", 0.3)
                        : "rgba(255,255,255,0.05)",
                      transform: "translateX(6px)",
                    },
                  }}
                >
                  <ListItemIcon
                    sx={{
                      minWidth: 40,
                      color: "rgba(255,255,255,0.6)",
                    }}
                  >
                    {item.icon}
                  </ListItemIcon>
                  <Typography fontSize="0.9rem">
                    {item.title}
                  </Typography>
                </ListItemButton>
              </ListItem>
            );
          })}
        </List>
      </Box>

      {/* ===== Logout (Fixed Bottom) ===== */}
      <Box sx={{ p: 2 }}>
        <ListItemButton
          onClick={handleLogout}
          sx={{
            borderRadius: 2.5,
            py: 1.5,
            px: 2,
            background:
              "linear-gradient(135deg, rgba(239, 68, 68, 0.15) 0%, rgba(220, 38, 38, 0.15) 100%)",
            border: "1px solid rgba(239, 68, 68, 0.2)",
            transition: "all 0.3s ease",
            "&:hover": {
              background:
                "linear-gradient(135deg, rgba(239, 68, 68, 0.25) 0%, rgba(220, 38, 38, 0.25) 100%)",
              border: "1px solid rgba(239, 68, 68, 0.4)",
              transform: "translateY(-2px)",
              boxShadow:
                "0 8px 24px rgba(239, 68, 68, 0.25)",
            },
          }}
        >
          <ListItemIcon sx={{ minWidth: 40, color: "#f87171" }}>
            <Logout />
          </ListItemIcon>
          <Typography>Logout</Typography>
        </ListItemButton>

        <Typography
          variant="caption"
          sx={{ display: "block", mt: 1, color: "rgba(255,255,255,0.4)" }}
        >
          © 2026 RZBBook. All Your Books.
        </Typography>
      </Box>
    </Box>
  );
};

export default SidebarDrawer;
