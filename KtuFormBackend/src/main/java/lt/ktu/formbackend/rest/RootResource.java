package lt.ktu.formbackend.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lukas
 */

@Path("/")
public class RootResource {

    @GET
    public Response rootPage() {
        return Response.ok("Rootinis url nieko nedaro            \n" +
"                               :oooooooooo:                           \n" +
"                        ::oOOOOooo:::::ooooO88Oo:                     \n" +
"                     :oOOo:                   :oO8O:                   \n" +
"                  :O8O:                           oO8o                 \n" +
"                O8O:                                 :OO:             \n" +
"              o8o                                      :8o             \n" +
"             88:                                         O8:           \n" +
"           o8o                                            :8o         \n" +
"          8O:                                               oO         \n" +
"         8o                                                  oO       \n" +
"        8o                                                    Oo       \n" +
"      :8o                                              :      :8:     \n" +
"     :8:                                               :       :8:     \n" +
"    :8:                                               :o        oO     \n" +
"    O::o                                              oo         8:   \n" +
"   :o:ooO                                             oo         o8   \n" +
"   8    oo                                            :o          8o   \n" +
"  oo     8o                                            OO:        :8   \n" +
"  8      o8:                                            O8O:       8: \n" +
" :o      :88                                             :8O       :O \n" +
" o:     ::88:                                             O8        8 \n" +
" 8       888:                                             O8o       O \n" +
":8       :88:                                             888Ooo:   oo \n" +
"oo       oO8:                                             888888o   :O \n" +
"Oo       O88           :oO88O:            oO88Oo:         O8888O    :O \n" +
"O:  ::   :88      :oO888888888:          8888888888OOo:    8888:    :O \n" +
"O:  :o    8o   o88888888888888:          88888888888888O:   o88     :O \n" +
"Oo   oo  oO  o888888888888888O           8888888888888888o   88     :O \n" +
":O    O: oo  8888888888888888:          o88888888888888888   88     :O \n" +
" 8    :o:8:  8888888888888888:::         :8888888888888888  o888o:::8O \n" +
" O:   :88:   O88888888888888888           O888888888888888  o88888888O \n" +
"  o   :8O    o888888888888888O            O88888888888888O   88888888o \n" +
"  O:  :8:    88888888888888O:      :      O888888888888888   O8888888: \n" +
"  :O  :8     88888888888888       o88:    :o88888888888888    8888888 \n" +
"   OoooO    :8888888888O:8o      :8O88       :O8888888888o     O888oo \n" +
"    888:     8888888o:  8O       :8o88O         O888888O:          :   \n" +
"    :88       oOOo:   :O:        :8:888          8o                   \n" +
"     88              o:          88oO88o      :: :oo            :     \n" +
"     88::               oo      O88O:888o    O8o:   :   ::     : :     \n" +
"     8o o:                     o88oo O888    :8        :88   :: ::     \n" +
"     oO  oo     o              O8o : O888     :o       oO8o ::  O     \n" +
"      O:  Oo  ::o              OO    OO :             :oO88 O::O:     \n" +
"       O  :8: :88o             OO    Oo             :O888O  8OO:       \n" +
"       oo  Oo  O888o           :O:o:  :            o8888o  :O         \n" +
"        :O888  :88888           :o  Oo            o8888o   8:         \n" +
"           :8:  O8888               8            :O888O   :8           \n" +
"            Oo  :8888               o             :888    :O           \n" +
"            :O   o888  ::                         o88:    Oo           \n" +
"            :O    O88OO8oOOoOOoOooOooOOOOOOOOOO88O88O     O:           \n" +
"            OO     888:: o :o: o  o   O: 8: O o::o 8:     8           \n" +
"           :8O     :88o:  :    :  :      ::        8      O           \n" +
"           O8o      Oo:8oo:    :          :   :::oo8     :O           \n" +
"           88o      Oo O :ooOOOoOO:OO   :8O:O O  o O     :O           \n" +
"           88o      Oo :      o:o: :o    Oo : :  o       :O           \n" +
"           88O       :oo:oo:o o :  :: oo :: o O::o       o:           \n" +
"           888         :::::oO8O8OoO8O88oOOoo:         :Oo             \n" +
"            O88Oo::               ::                  :O               \n" +
"             :oO8888o                               :Oo               \n" +
"                 :o88o                      oo    o8O:                 \n" +
"                    O8o                     Oo  o88o                   \n" +
"                     o8O     Oo    o           o88                     \n" +
"                      :88o        O:          O88                     \n" +
"                        O88o     :8o        o88O                       \n" +
"                          o88ooOO888O::::oO88O:                       \n" +
"                            o8888888888888Oo                  ", MediaType.TEXT_PLAIN).build();
    }
}
