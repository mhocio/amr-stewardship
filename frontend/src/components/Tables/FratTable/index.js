import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { styled } from "@mui/material";

export default function FratTable({ fratData }) {
  return (
    <TableContainer
      component={Paper}
      sx={{
        width: "96vw"
      }}
    >
      <Table
        ssx={{
          width: "max-content"
        }}
        size="small"
        aria-label="frat table"
      >
        <TableHead>
          <TableRow sx={{ background: "#D6E4FC" }}>
            <TableCell sx={{ position: "sticky", left: 0, backgroundColor: "#D6E4FC" }} rowSpan={3}>
              Szczep bakteryjny
            </TableCell>
            <TableCell
              rowSpan={3}
              align="center"
            >
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
              <TableCell component="th" scope="row" sx={{ position: "sticky", left: 0, backgroundColor: "#D6E4FC" }}>
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
            <TableCell sx={{ position: "sticky", left: 0, backgroundColor: "#D6E4FC" }}>Razem</TableCell>
            <TableCell align="center">432</TableCell>
            <TableCell />
            <TableCell align="center">43</TableCell>
            <TableCell />
            <TableCell align="center">55</TableCell>
          </TableRow>
          <TableRow sx={{ background: "#D6E4FC", height: 45 }}>
            <TableCell sx={{ position: "sticky", left: 0, backgroundColor: "#D6E4FC" }}>F/I [%] aktywności ogólnej</TableCell>
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
