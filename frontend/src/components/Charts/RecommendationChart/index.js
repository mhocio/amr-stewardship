import React, { useCallback } from "react";

// material-ui
import { Paper, Grid } from "@mui/material";

// third-party
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";
import { useCurrentPng } from "recharts-to-png";
import FileSaver from "file-saver";

// project imports
import DownloadChartButton from "../../DownloadChartButton";

export default function RecommendationChart({ graphData }) {
  const [getPng, { ref: myRef }] = useCurrentPng();
  const handleDownload = useCallback(async () => {
    const png = await getPng();
    if (png) {
      FileSaver.saveAs(png, "wykres.png");
    }
  }, [getPng]);

  console.log(graphData);

  return (
    <Grid container direction="column" spacing={3} wrap="nowrap">
      <Grid item xs={9}>
        <Paper sx={{ padding: "20px" }}>
          <ResponsiveContainer width="99%" aspect={1} maxHeight={500}>
            <LineChart
              ref={myRef}
              width={500}
              height={300}
              data={graphData}
              margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="Nazwa" />
              <YAxis />
              <Tooltip />
              <Legend />
              {graphData[0]
                ? Object.keys(graphData[0]).map((key, index, arr) =>
                    key != "Nazwa" ? (
                      <Line
                        type="monotone"
                        dataKey={key}
                        stroke={"#000000".replace(/0/g, function () {
                          return (~~(Math.random() * 16)).toString(16);
                        })}
                      />
                    ) : null
                  )
                : null}
            </LineChart>
          </ResponsiveContainer>
        </Paper>
      </Grid>
      <Grid item>
        <DownloadChartButton handle={handleDownload} />
      </Grid>
    </Grid>
  );
}
