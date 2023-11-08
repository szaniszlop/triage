import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'

const initialState = {
  data: [],
  status: "pending",
  error: "",
  info: ""
}

export const fetchInvoices = createAsyncThunk('invoices/fetchInvoices', async (client) => {
  console.log("fetchInvoices", client)
  const response = await client.getInvoices()
  console.log("fetchInvoices", response)
  return response
})

export const invoiceAdded = createAsyncThunk('invoices/invoiceAdded', async ({apiClient, body}) => {
  console.log("invoiceAdded", apiClient, body)
  const response = await apiClient.addInvoice(body)
  console.log("invoiceAdded", response)
  return response
})

const invoiceSlice = createSlice({
  name: 'invoices',
  initialState,
  reducers: {
    invoiceUpdated( state, action){
        console.log("updateInvoice", action);
        state.data.map((invoice) => {return invoice.number === action.payload.number ? action.payload : invoice});
    },
    invoiceDeleted( state, action){
        console.log("deleteInvoice", action);
        state.data.filter((invoice) => {return invoice.number !== action.payload.number });
    },
    resetStatus(state, action){
      state.status = "pending"
      state.error = ""
    }
  },
  extraReducers(builder) {
    builder
    .addCase(fetchInvoices.pending, (state, action) =>{
      console.log("fetchInvoice pending");
      state.status = "loading";
    })
    .addCase(fetchInvoices.fulfilled, (state, action) => {
      console.log("fetchInvoice loaded");
      state.status = "loaded"
      state.data = action.payload;
    })
    .addCase(fetchInvoices.rejected, (state, action) => {
      console.log("fetchInvoice failed");
      state.status = "failed"
      state.error = action.error.message
      state.data = []
    })
    .addCase(invoiceAdded.rejected, (state, action) => {
      console.log("invoiceAdded failed");
      state.error = action.error.message
    })
    .addCase(invoiceAdded.fulfilled, (state, action) => {
      console.log("invoiceAdded filfilled");
      // new invoice added - just reload the whole list
      state.status = "pending";
    })
  }
})

export const { invoiceUpdated,  invoiceDeleted, resetStatus} = invoiceSlice.actions

export const selectInvoices = state => state.invoices.data;
export const selectInvoicesStatus = state => state.invoices.status;
export const selectInvoicesError = state => state.invoices.error;
export const selectInvoicesInfo = state => state.invoices.info;
export const selectInvoice = (number) => (state) => state.invoices.data.find( invoice => invoice.number === number);

export default invoiceSlice.reducer
