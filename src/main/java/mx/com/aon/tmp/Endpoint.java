package mx.com.aon.tmp;

import mx.com.gseguros.exception.DaoException;

public abstract interface Endpoint {
	public abstract Object invoke(Object paramObject)
			throws DaoException;

	public abstract void setService(Service paramService);

	public abstract Service getService();
}