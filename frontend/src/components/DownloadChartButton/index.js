import * as React from "react";

// material-ui
import { Button, Stack } from "@mui/material";
import DownloadIcon from "@mui/icons-material/Download";

export default function DownloadChartButton({ handle }) {
  return (
    <Stack direction="row" spacing={2}>
      <Button
        variant="outlined"
        onClick={() => handle()}
        startIcon={<DownloadIcon />}
      >
        Pobierz wykres
      </Button>
    </Stack>
  );
}
