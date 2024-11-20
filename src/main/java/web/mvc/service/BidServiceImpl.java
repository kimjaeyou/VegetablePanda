package web.mvc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.domain.Bid;
import web.mvc.repository.BidRepository;

@Service
@RequiredArgsConstructor

public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;


    /*
        입찰시도
     */
    @Override
    public Bid bid(Bid newBidder) {
        return bidRepository.save(newBidder);
    }





}
