import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { styled } from "@mui/material";

const StyledTableRow = styled("TableRow")({
  root: {
    "&:nth-of-type(odd)": {
      backgroundColor: "white"
    },
    "&:nth-of-type(even)": {
      backgroundColor: "grey"
    }
  }
});

export default function FratTable({ fratData }) {

  return (
    <TableContainer component={Paper}>
      <Table
        sx={{ minWidth: 650, overflowX: "auto" }}
        size="small"
        aria-label="a dense table"
      >
        <TableHead>
          <TableRow sx={{ background: "#D6E4FC" }}>
            <TableCell rowSpan={3}>Szczep bakteryjny</TableCell>
            <TableCell rowSpan={3} align="center">
              %I
            </TableCell>
            {fratData?.antibiotics?.map((item, i) => {
              return (
                <TableCell colSpan={2} align="center">
                  {item}
                </TableCell>
              );
            })}
          </TableRow>
          <TableRow sx={{ background: "#EBF2FE" }}>
            {fratData?.antibiotics?.map((item, i) => {
              return (
                <>
                  <TableCell align="center">%S</TableCell>
                  <TableCell align="center">F</TableCell>
                </>
              );
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {fratData?.rows?.map((row) => (
            <TableRow
              key={row.name}
              sx={{
                "&:last-child td, &:last-child th": { border: 0 },
                "&:nth-of-type(even)": {
                  background: "#EBF2FE"
                }
              }}
            >
              <TableCell component="th" scope="row">
                {row[0]}
              </TableCell>
              {row.slice(1).map((item, i) => {
                return <TableCell align="center">{item}</TableCell>;
              })}
            </TableRow>
          ))}
          <TableRow
            sx={{
              "&:nth-of-type(even)": {
                background: "#EBF2FE"
              },
              height: 45
            }}
          >
            <TableCell>Razem</TableCell>
            <TableCell align="center">432</TableCell>
            <TableCell />
            <TableCell align="center">43</TableCell>
            <TableCell />
            <TableCell align="center">55</TableCell>
          </TableRow>
          <TableRow sx={{ background: "#D6E4FC", height: 45 }}>
            <TableCell>F/I [%] aktywności ogólnej</TableCell>
            <TableCell align="center">432</TableCell>
            <TableCell />
            <TableCell align="center">43</TableCell>
            <TableCell />
            <TableCell align="center">55</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
