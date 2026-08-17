import spock.lang.Specification
import spock.lang.Unroll

class teste_do_spock extends Specification{


    def "teste do spock"() {
        expect:
            1==1
        //https://www.jetbrains.com/help/idea/spock.html#simple_assertion
    }

    @Unroll
    def "mais um teste: #a é diferente de  #b"() {
        expect:
            a != b

        where:
            a | b
            0 | 1
            2 | 3
            4 | 5
    }

    def "mais um teste: a é igual a b"() {
        expect:
            a == b

        where:
            a | b
            0 | 0
            1 | 1
            2 | 2
    }

    @Unroll
    def "quarto teste: #input precede #output" () {
        given:
            def aura = {it+1}

        when:
            def res = aura(input)

        then:
            res == output

        where:
            input | output
            0     | 1
            1     | 2
    }

    @Unroll
    def "quinto teste: #input sucsede #output" () {
        given:
            def aura = {it-1}

        expect:
            aura(input) == output

        where:
            input | output
            1     | 0
            0     | -1
    }


    //isso é muito bizarro
}
