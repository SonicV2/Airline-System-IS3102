/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.BoardingPass;
import Inventory.Entity.Booking;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface BoardingPassSessionBeanLocal {

    public void addBooking(Booking booking);

    public void addSeat(Booking booking, String seatNo);

    public BoardingPass findBoardingpass(Booking booking);

    public BoardingPass retrieveBoardingPassByBooking(Booking booking);

    public void changeBoardingPassClass(Booking booking, String sClass);
}
