package epic.easystock.data;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.google.api.client.util.DateTime;

import epic.easystock.apiEndpoint.model.Product;
import epic.easystock.io.EndPointCall;

public abstract class LocalObject {
	public enum DataState {
		UNCHANGED, CHANGED, NEW, FINAL, FETCH_FROM_CLOUD; //0 1 2
		/*private int vaule;
		private static Map<Integer, LegNo> map = new HashMap<Integer, LegNo>();
		static {
			for (LegNo legEnum : LegNo.values()) {
				map.put(legEnum.legNo, legEnum);
			}
		}*/
		
		/*private DataState(final int vaule) {
			this.vaule = vaule;
		}*/
		/*public static DataState valueOf(int i) {
			DataState.valueOf()
			if(i==0) return UNCHANGED;
			if(i==1) return CHANGED;
			if(i==2) return NEW;
			throw new RuntimeException();
		}
		public int valueOf() {
			return this.ordinal();
		}**/
	}
	protected static final String STR_O_LONG_KEY = "key_id";					//long
	protected static final String STR_O_INT_CHANGED = "changed";				//int
	protected static final String STR_O_LONG_TIMESTAMP = "timestamp";			//long

	protected static final String STR_MP_REAL_AMOUNT = "amount";				//real
	protected static final String STR_MP_LONG_PRODUCT_KEY = "product_key";	//real

	protected static final String STR_P_STRING_NAME = "name";					//String
	protected static final String STR_P_STRING_DESCRIPTION = "description";	//String
	protected static final String STR_P_LONG_BARCODE = "barcode";				//long
	
	private final Long key;
	private DataState dataState;
	private DateTime timestamp;
	
	public LocalObject(Long key, DataState dataState, DateTime timestamp) {
		super();
		this.key = key;
		this.dataState = dataState;
		this.timestamp = timestamp;
	}
	public LocalObject(Cursor cursor) {
		super();
		this.key = cursor.getLong(cursor.getColumnIndex(STR_O_LONG_KEY));
		this.dataState = DataState.values()[cursor.getInt(cursor.getColumnIndex(STR_O_INT_CHANGED))];//FIXME não gosto to nasted
		this.timestamp = new DateTime(cursor.getLong(cursor.getColumnIndex(STR_O_LONG_TIMESTAMP)));
	}

	public LocalObject(Product product) {
		this.key = product.getKey().longValue();
		this.dataState = DataState.FETCH_FROM_CLOUD;
		this.timestamp = product.getTimeStamp();
	}
	public LocalObject() {
		this.key = EndPointCall.nextLocalObjectKey();
		this.dataState = DataState.NEW;
		this.timestamp = new DateTime(new Date().getTime());
	}
	protected ContentValues getContentValues() {
		ContentValues aux = new ContentValues();
		aux.put(STR_O_LONG_KEY, this.getKey());
		aux.put(STR_O_INT_CHANGED, this.getDataState().ordinal());
		aux.put(STR_O_LONG_TIMESTAMP, this.getTimestamp().getValue());
		return aux;
	}
	
	public Long getKey() {return key;}
	public DataState getDataState() {return dataState;}
	public DateTime getTimestamp() {return timestamp;}
	protected void modified() { //NÂO GOSTOS DOS IFS !!! FIXME
		this.timestamp = new DateTime(new Date());
		this.dataState = (dataState == DataState.NEW) ? DataState.NEW : DataState.CHANGED;
		if(this.dataState == DataState.UNCHANGED)
			this.dataState = DataState.CHANGED;
		else if (this.dataState == DataState.CHANGED)
			;//NONE
		else if (this.dataState == DataState.FETCH_FROM_CLOUD)
			this.dataState = DataState.CHANGED; //FIXME CONFIRMAR
		else if (this.dataState == DataState.NEW)
			;//NONE
		else if (this.dataState == DataState.FINAL)
			throw new RuntimeException(); //FIXME
	}
}
