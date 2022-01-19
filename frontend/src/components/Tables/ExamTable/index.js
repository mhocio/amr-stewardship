import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import { Typography, Grid, Paper, Skeleton, Box } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import TopicIcon from "@mui/icons-material/Topic";
import BASE_URL from "../../../constants/BASE_URL";

import { StyledDataGrid } from "../../../styledComponents/StyledDataGrid";

const columns = [
  { field: "antibiogramId", headerName: "ID antybiogramu", width: 140 },
  { field: "pesel", headerName: "PESEL", width: 110 },
  { field: "ward", headerName: "Oddział", width: 150 },
  { field: "bacteria", headerName: "Drobnoustrój", width: 250 },
  { field: "antibiotic", headerName: "Antybiotyk", width: 300 },
  { field: "material", headerName: "Materiał", width: 300 },
  { field: "orderDate", headerName: "Data zlecenia", width: 120 },
  { field: "susceptibility", headerName: "Wrażliwość", width: 120 }
];

export default function ExamTable({ data, loading, patientAntibiograms }) {
  var skeletons = [];
  for (var i = 0; i < 12; i++) {
    skeletons.push(<Skeleton key={i} />);
  }

  return (
    <>
      <Paper sx={{ padding: "20px", paddingBottom: "70px" }}>
        <div style={{ height: "60vh", width: "100%" }}>
          <Grid
            container
            direction="row"
            alignItems="center"
            spacing={1}
            wrap="nowrap"
          >
            <Grid item>
              <TopicIcon fontSize="large" />
            </Grid>
            <Grid item>
              <Typography variant="h5">Antybiogramy</Typography>
            </Grid>
          </Grid>
          {loading ? (
            <Box sx={{ height: "100%" }}>
              <Skeleton height={70} />
              {skeletons}
            </Box>
          ) : (
            <StyledDataGrid
              rows={data}
              columns={columns}
              responsive={"scrollMaxHeight"}
              hideFooter={false}
              rowHeight={30}
            />
          )}
        </div>
      </Paper>
    </>
  );
}
