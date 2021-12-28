import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material'

function createData(name, calories, fat, carbs, protein, hey) {
  return { name, calories, fat, carbs, protein, hey };
}

const rows = [
  createData('Frozen yoghurt', 159, 6.0, 24, 4.0, 5.3),
  createData('Ice cream sandwich', 237, 9.0, 37, 4.3, 7.6),
  createData('Eclair', 262, 16.0, 24, 6.0, 6.3),
  createData('Cupcake', 305, 3.7, 67, 4.3, 5.4),
  createData('Gingerbread', 356, 16.0, 49, 3.9, 6.6),
];

const StyledTableRow = styled('TableRow')({
  root: {
    '&:nth-of-type(odd)': {
      backgroundColor: "white",
    },
    '&:nth-of-type(even)': {
      backgroundColor: "grey",
    },
  },
});

export default function DenseTable() {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow sx={{ background: "#F4F5F7" }}>
            <TableCell rowSpan={3}>Szczep bakteryjny</TableCell>
            <TableCell rowSpan={3} align="center">%I</TableCell>
            <TableCell colSpan={2} align="center">
              Ampicylina
            </TableCell>
            <TableCell colSpan={2} align="center">
              Netilmycyna
            </TableCell>
          </TableRow>
          <TableRow sx={{ background: "#E7EBEF" }}>
            <TableCell align="center">%S</TableCell>
            <TableCell align="center">F</TableCell>
            <TableCell align="center">%S</TableCell>
            <TableCell align="center">F</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow
              key={row.name}
              sx={{
                '&:last-child td, &:last-child th': { border: 0 },
                '&:nth-of-type(even)': {
                  background: "#F4F5F7",
                }
              }}
            >
              <TableCell component="th" scope="row">{row.name}</TableCell>
              <TableCell align="center">{row.calories}</TableCell>
              <TableCell align="center">{row.fat}</TableCell>
              <TableCell align="center">{row.carbs}</TableCell>
              <TableCell align="center">{row.protein}</TableCell>
              <TableCell align="center">{row.hey}</TableCell>
            </TableRow>
          ))}
          <TableRow sx={{                '&:nth-of-type(even)': {
                  background: "#F4F5F7",
                } , height: 45 }}>
            <TableCell>Razem</TableCell>
            <TableCell align="center">432</TableCell>
            <TableCell />
            <TableCell align="center">
              43
            </TableCell>
            <TableCell />
            <TableCell align="center">
              55
            </TableCell>
          </TableRow>
          <TableRow sx={{ background: "#D1D8E1", height: 45}}>
            <TableCell>F/I [%] aktywności ogólnej</TableCell>
            <TableCell align="center">432</TableCell>
            <TableCell />
            <TableCell align="center">
              43
            </TableCell>
            <TableCell />
            <TableCell align="center">
              55
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
