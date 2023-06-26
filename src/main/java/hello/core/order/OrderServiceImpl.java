package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    // 클라이언트는 DiscountPolicy를 의존하는 것처럼 보이지만 실제로는 구체 인터페이스인 FixDiscountPolicy, RateDiscountPolicy에도 의존하고 있다.
    // -> DIP 위반
    // 할인 정책을 변경하는 순간 OrderServiceImpl 을 수정해야 한다. -> OCP 위반
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    //DIP를 위반하지 않도록 인터페이스만 의존하도록 할당하면 된다.
    private final DiscountPolicy disCountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy disCountPolicy) {
        this.memberRepository = memberRepository;
        this.disCountPolicy = disCountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // 할인 정책은 discountPolicy에 일임할 수 있으므로 단일 체계 원칙이 잘 설계되어있다고 할 수 있다.
        int discountPrice = disCountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
