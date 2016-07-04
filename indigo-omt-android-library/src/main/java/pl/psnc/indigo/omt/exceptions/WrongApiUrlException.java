package pl.psnc.indigo.omt.exceptions;

/**
 * Created by michalu on 23.03.16.
 */
public class WrongApiUrlException extends IndigoException {
    public WrongApiUrlException() {
        super("Wrong api url. Please check the domain and port (e.x. http://domain.com:8888)");
    }
}
