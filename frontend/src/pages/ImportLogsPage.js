import React, { useState, useEffect, useRef } from "react";

import BASE_URL from "../constants/BASE_URL";
import { useLoading } from "../loading/loading-context";
import axios from "../services/interceptor";
import authHeader from "../services/auth-header";
import ReactJson from 'react-json-view';

import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";

const ImportLogsPage = () => {

    const [logsData, setLogsData] = useState([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        axios
            .get(`${BASE_URL}/ImportDataInfo`, {
                method: "GET",
                mode: "cors",
                headers: {
                    Accept: "application/json, text/plain",
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*",
                    ...authHeader()
                }
            })
            .then((res) => {
                console.log(res);
                setLogsData(res.data);
            })
            .catch((err) => {
                console.log(err);
            })
            .finally(() => {

            });
    }

    return (
        <>
            <DrawerHeader />
            <ReactJson src={logsData} collapsed="true"/>
        </>
        
    );

}

export default ImportLogsPage;