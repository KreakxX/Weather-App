import axios from "axios";
import { url } from "inspector";

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1/Weather',
});


export const getWeatherData = async(location: string)=>{
  try{
    const Api_Response = await api.get(`/get/${location}`)
    return Api_Response.data;
  }catch(error){
    console.log(error);
    throw error;
  }
}

export const GetWeatherForecast = async(location : string) =>{
  try{
    const Api_Response = await api.get(`/get/forecast/${location}`)
    return Api_Response.data;
  }catch(error){
    console.log(error);
    throw error;
  }
}

export const getWeeklyTempForecast = async(location: string)=>{
  try{
    const Api_Response = await api.get(`/get/weekly/min/max/temp/${location}`);
    return Api_Response.data;
  }catch(error){
    console.log(error);
    throw error;
  }
}