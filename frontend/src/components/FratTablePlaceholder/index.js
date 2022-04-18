import React from "react";

// material-ui
import { Box, Typography, Grid } from "@mui/material";

export default function FratTablePlaceholder() {
  return (
    <Box
      sx={{
        padding: "50px",
        border: 1,
        borderStyle: "dashed",
        borderRadius: 2,
        borderColor: "grey.500",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "20vh"
      }}
    >
      <Grid container justifyContent="center" alignItems="center">
        <Grid item>
          <Typography variant="caption">
            Naciśnij przycisk aby uzyskać analizę dla wybranych powyżej
            informacji.
          </Typography>
        </Grid>
      </Grid>
    </Box>
  );
}
